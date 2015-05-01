package ee.lis;

import static ee.lis.TestUtils.Analysis;
import static ee.lis.TestUtils.Container;
import static ee.lis.TestUtils.MyLabResultMsg;
import static ee.lis.TestUtils.Order;
import static ee.lis.TestUtils.Patient;
import static ee.lis.TestUtils.Result;
import static ee.lis.TestUtils.*;
import static java.util.Arrays.asList;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import ee.lis.interfaces.MyLabMessages.*;
import ee.lis.mock.MockLIS;
import ee.lis.mock.MockSocketAnalyzer;
import ee.lis.mock.MockSocketAnalyzer.Mode;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class LIS2A2OverTcpTest {
    private static ActorSystem system;
    private static MockSocketAnalyzer mockAnalyzer;
    private static MockLIS mockLIS;
    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("testSystem");
        mockAnalyzer = new MockSocketAnalyzer(system, "127.0.0.1", 50_000, Mode.CLIENT);
        mockLIS = new MockLIS(system, "127.0.0.1", 8070);

//        ActorRef astmDriver = system.actorOf(Props.create(DynamicDriver.class), "astmDriver");
        Config config = ConfigFactory.load().getConfig("Drivers.AstmOverTcp")
            .withValue("SocketServer.address", ConfigValueFactory.fromAnyRef("127.0.0.1"))
            .withValue("SocketServer.port", ConfigValueFactory.fromAnyRef(50_000))
            .withValue("MyLabHttpClient.queryUrl", ConfigValueFactory.fromAnyRef("http://localhost:8070/query"))
            .withValue("MyLabHttpClient.resultUrl", ConfigValueFactory.fromAnyRef("http://localhost:8070/result"));
//        astmDriver.tell(config, ActorRef.noSender());
    }

    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void receiveResult() {
        new JavaTestKit(system) {{
            mockAnalyzer
                .send("<ENQ>")
                .expect("<ACK>")
                .send("<STX>0H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>1P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>2O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|19890501153023|19890502070000|||456^Farnsworth|N|||19890502113000|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<ETX><CR><LF>")
                .expect("<ACK>")
                .send("<STX>3R|1|^^^Org#|51|||N<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>4R|2|^^^Bio|BH+|||N<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>5L|1<CR><ETX>4A<CR><LF>")
                .expect("<ACK>")
                .send("<EOT>")
                .expectNoMsg();

            mockLIS.expect(
                MyLabResultMsg(
                    Order(
                        Patient("John", "Smith", "52483291"),
                        containers(
                            Container("5762",
                                      analyses(
                                          Analysis("Org#", "", Result("51", "")),
                                          Analysis("Bio", "", Result("BH+", ""))
                                      )
                            )
                        )
                    )
                )
            ).send("");
        }};
    }

    @Test
    public void queryAndReceiveOrder() {
        new JavaTestKit(system) {{
            mockAnalyzer
                .send("<ENQ>")
                .expect("<ACK>")
                .send("<STX>1H|\\^&||PSWD|Harper Labs|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722||||P|1394-97|19890314121122<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>1Q|1|^SpecimenID1\\^SpecimenID2||^^^ALL<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<STX>2L|1<CR><ETX>{CS}<CR><LF>")
                .expect("<ACK>")
                .send("<EOT>")
                .expectNoMsg();

            mockLIS
                .expect(new MyLabQueryMsg(asList("SpecimenID1", "SpecimenID2"), asList("ALL")))
                .send(new MyLabOrderMsg(new Order(new Patient("Allen", "Pohl", "12345"),
                    asList(new Container("SpecimenID1", asList(new Analysis("An1", "Analysis1", null), new Analysis("An2", "Analysis2", null))),
                        new Container("SpecimenID2", asList(new Analysis("An5", "Analysis5", null)))))));

            mockAnalyzer
                .expect("<ENQ>")
                .send("<ACK>")
                .expect("<STX>0H|\\^&|12345||MyLIS|York 15^London^^89983^||||Analyzer||P|LIS2-A2|" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "<CR><ETX>{CS}<CR><LF>")
                .send("<ACK>")
                .expect("<STX>1P|1||12345||Pohl^Allen<CR><ETX>{CS}<CR><LF>")
                .send("<ACK>")
                .expect("<STX>2O|1|SpecimenID1||^Analysis1^^An1\\^Analysis2^^An2|P<CR><ETX>{CS}<CR><LF>")
                .send("<ACK>")
                .expect("<STX>3O|2|SpecimenID2||^Analysis5^^An5|P<CR><ETX>{CS}<CR><LF>")
                .send("<ACK>")
                .expect("<STX>4L|1<CR><ETX>{CS}<CR><LF>")
                .send("<ACK>")
                .expect("<EOT>");
        }};
    }
}