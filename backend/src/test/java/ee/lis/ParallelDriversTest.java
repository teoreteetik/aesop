package ee.lis;

import static com.typesafe.config.ConfigValueFactory.fromAnyRef;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ee.lis.core.Master;
import ee.lis.interfaces.MyLabMessages.*;
import ee.lis.mock.MockLIS;
import ee.lis.mock.MockSocketAnalyzer;
import ee.lis.mock.MockSocketAnalyzer.Mode;
import ee.lis.util.JsonUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;


public class ParallelDriversTest {

    @Test
    public void parallelDriversTest() throws Throwable {
        Class[] cls = {ParallelTest.class};

        org.junit.runner.Result result = JUnitCore.runClasses(ParallelComputer.methods(), cls);
        int errors = result.getFailureCount();
        if (errors != 0) {
            System.err.println(errors + " errors!");
            for (Failure f : result.getFailures())
                f.getException().printStackTrace();
            throw result.getFailures().get(0).getException();
        }
    }

    public static class ParallelTest {
        private static ActorSystem system;
        private static MockSocketAnalyzer mockAnalyzer1;
        private static MockSocketAnalyzer mockAnalyzer2;
        private static MockSocketAnalyzer mockAnalyzer3;
        private static MockSocketAnalyzer mockAnalyzer4;
        private static MockSocketAnalyzer mockAnalyzer5;
        private static MockSocketAnalyzer mockAnalyzer6;
        private static MockSocketAnalyzer mockAnalyzer7;
        private static MockSocketAnalyzer mockAnalyzer8;
        private static MockSocketAnalyzer mockAnalyzer9;
        private static MockSocketAnalyzer mockAnalyzer10;
        private static MockLIS mockLIS;

        @BeforeClass
        public static void setup() {
            system = ActorSystem.create("testSystem", ConfigFactory.parseString("akka.loglevel = OFF"));
            mockAnalyzer1 = new MockSocketAnalyzer(system, "127.0.0.1", 50_001, Mode.CLIENT);
            mockAnalyzer2 = new MockSocketAnalyzer(system, "127.0.0.1", 50_002, Mode.CLIENT);
            mockAnalyzer3 = new MockSocketAnalyzer(system, "127.0.0.1", 50_003, Mode.CLIENT);
            mockAnalyzer4 = new MockSocketAnalyzer(system, "127.0.0.1", 50_004, Mode.CLIENT);
            mockAnalyzer5 = new MockSocketAnalyzer(system, "127.0.0.1", 50_005, Mode.CLIENT);
            mockAnalyzer6 = new MockSocketAnalyzer(system, "127.0.0.1", 50_006, Mode.CLIENT);
            mockAnalyzer7 = new MockSocketAnalyzer(system, "127.0.0.1", 50_007, Mode.CLIENT);
            mockAnalyzer8 = new MockSocketAnalyzer(system, "127.0.0.1", 50_008, Mode.CLIENT);
            mockAnalyzer9 = new MockSocketAnalyzer(system, "127.0.0.1", 50_009, Mode.CLIENT);
            mockAnalyzer10 = new MockSocketAnalyzer(system, "127.0.0.1", 50_010, Mode.CLIENT);
            mockLIS = new MockLIS(system, "127.0.0.1", 8070);

            Config analyzerConfig = ConfigFactory.parseResources("driverDefinitions/LIS2A2OverTcp.conf")
                .withValue("MyLabHttpClient.resultUrl", fromAnyRef("http://localhost:8070/result"))
                .withValue("MyLabHttpClient.queryUrl", fromAnyRef("http://localhost:8070/query"))
                .withValue("SocketServer.address", fromAnyRef("127.0.0.1"));

            Config drivers = ConfigFactory.empty()
                .withValue("Analyzer1", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_001)).root())
                .withValue("Analyzer2", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_002)).root())
                .withValue("Analyzer3", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_003)).root())
                .withValue("Analyzer4", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_004)).root())
                .withValue("Analyzer5", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_005)).root())
                .withValue("Analyzer6", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_006)).root())
                .withValue("Analyzer7", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_007)).root())
                .withValue("Analyzer8", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_008)).root())
                .withValue("Analyzer9", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_009)).root())
                .withValue("Analyzer10", analyzerConfig.withValue("SocketServer.port", fromAnyRef(50_010)).root());


            Config config = ConfigFactory.load().withValue("DriverManager", drivers.root());
            system.actorOf(Props.create(Master.class, config));
        }

        @AfterClass
        public static void tearDown() {
            JavaTestKit.shutdownActorSystem(system);
            system = null;
        }

        @Test
        public void validateMessagesReceivedByLIS() {
            List<String> lisExpectedMsgs = new ArrayList<>();
            String resultJson = JsonUtil.asJson(
                new MyLabResultMsg(new Order(new Patient("John", "Smith", "52483291"),
                    singletonList(new Container("5762",
                        asList(new Analysis("Org#", "", new Result("51", "")),
                            new Analysis("Bio", "", new Result("BH+", ""))))))));
            String queryJson = JsonUtil.asJson(
                new MyLabQueryMsg(asList("SpecimenID1", "SpecimenID2"), singletonList("ALL"))
            );
            MyLabOrderMsg orderMsg = new MyLabOrderMsg(new Order(new Patient("Allen", "Pohl", "12345"),
                asList(new Container("SpecimenID1", asList(new Analysis("An1", "Analysis1", null), new Analysis("An2", "Analysis2", null))),
                    new Container("SpecimenID2", singletonList(new Analysis("An5", "Analysis5", null))))));

            for (int i = 0; i < 10; i++) {
                lisExpectedMsgs.add(resultJson);
                lisExpectedMsgs.add(queryJson);
            }

            while (!lisExpectedMsgs.isEmpty()) {
                String receivedMsg = mockLIS.expectAnyString();
                lisExpectedMsgs.remove(receivedMsg);
                if (receivedMsg.equals(resultJson))
                    mockLIS.send("");
                else if (receivedMsg.equals(queryJson))
                    mockLIS.send(orderMsg);
            }
        }

        @Test public void testAnalyzer1()  { executeSequence(mockAnalyzer1);  }
        @Test public void testAnalyzer2()  { executeSequence(mockAnalyzer2);  }
        @Test public void testAnalyzer3()  { executeSequence(mockAnalyzer3);  }
        @Test public void testAnalyzer4()  { executeSequence(mockAnalyzer4);  }
        @Test public void testAnalyzer5()  { executeSequence(mockAnalyzer5);  }
        @Test public void testAnalyzer6()  { executeSequence(mockAnalyzer6);  }
        @Test public void testAnalyzer7()  { executeSequence(mockAnalyzer7);  }
        @Test public void testAnalyzer8()  { executeSequence(mockAnalyzer8);  }
        @Test public void testAnalyzer9()  { executeSequence(mockAnalyzer9);  }
        @Test public void testAnalyzer10() { executeSequence(mockAnalyzer10); }

        private void executeSequence(MockSocketAnalyzer mockAnalyzer) {
            queryAndReceiveOrder(mockAnalyzer);
            receiveResult(mockAnalyzer);
        }

        private void queryAndReceiveOrder(MockSocketAnalyzer mockAnalyzer) {
            new JavaTestKit(system) {{
                mockAnalyzer
                    .send("<ENQ>")
                    .expect("<ACK>")
                    .send("<STX>H|\\^&||PSWD|Harper Labs|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722||||P|1394-97|19890314121122<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>Q|1|^SpecimenID1\\^SpecimenID2||^^^ALL<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>L|1<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<EOT>");

                mockAnalyzer
                    .expect("<ENQ>")
                    .send("<ACK>")
                    .expect("<STX>H|\\^&|12345||MyLIS|York 15^London^^89983^||||Analyzer||P|LIS2-A2|" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "<CR><ETX>{CS}<CR><LF>")
                    .send("<ACK>")
                    .expect("<STX>P|1||12345||Pohl^Allen<CR><ETX>{CS}<CR><LF>")
                    .send("<ACK>")
                    .expect("<STX>O|1|SpecimenID1||^Analysis1^^An1\\^Analysis2^^An2|P<CR><ETX>{CS}<CR><LF>")
                    .send("<ACK>")
                    .expect("<STX>O|2|SpecimenID2||^Analysis5^^An5|P<CR><ETX>{CS}<CR><LF>")
                    .send("<ACK>")
                    .expect("<STX>L|1<CR><ETX>{CS}<CR><LF>")
                    .send("<ACK>")
                    .expect("<EOT>");
            }};
        }

        private void receiveResult(MockSocketAnalyzer mockAnalyzer) {
            new JavaTestKit(system) {{
                mockAnalyzer
                    .send("<ENQ>")
                    .expect("<ACK>")
                    .send("<STX>H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|19890501153023|19890502070000|||456^Farnsworth|N|||19890502113000|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<ETX><CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>R|1|^^^Org#|51|||N<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>R|2|^^^Bio|BH+|||N<CR><ETX>{CS}<CR><LF>")
                    .expect("<ACK>")
                    .send("<STX>L|1<CR><ETX>4A<CR><LF>")
                    .expect("<ACK>")
                    .send("<EOT>");
            }};
        }
    }
}