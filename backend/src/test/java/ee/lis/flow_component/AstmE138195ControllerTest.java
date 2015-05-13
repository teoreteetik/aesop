package ee.lis.flow_component;

import static akka.actor.ActorRef.noSender;
import static ee.lis.TestUtils.convertLowLevelChars;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.typesafe.config.ConfigFactory;
import ee.lis.flow_component.astme138195.AstmE138195Controller;
import ee.lis.flow_component.astme138195.AstmE138195ControllerConf;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

public class AstmE138195ControllerTest {

    @Test
    public void parallelTest() throws Throwable {
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
        private static JavaTestKit lowlevelProbe;
        private static JavaTestKit highlevelProbe;
        private static ActorRef astmE138195Controller;
        private static ActorPair actorPair;

        @BeforeClass
        public static void setup() {
            system = ActorSystem.create("testSystem", ConfigFactory.parseString("akka.loglevel = OFF"));
            lowlevelProbe = new JavaTestKit(system);
            highlevelProbe = new JavaTestKit(system);

            astmE138195Controller = system.actorOf(Props.create(AstmE138195Controller.class));
            astmE138195Controller.tell(new AstmE138195ControllerConf(240, lowlevelProbe.getRef(), highlevelProbe.getRef()), noSender());
            actorPair = new ActorPair(astmE138195Controller, lowlevelProbe);

            for (int i = 0; i < 4; i++) {
                astmE138195Controller.tell(convertLowLevelChars(
                    "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                        "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                        "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                        "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                        "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                        "L|1<CR>"), noSender());
            }
        }

        @AfterClass
        public static void tearDown() {
            JavaTestKit.shutdownActorSystem(system);
            system = null;
        }

        @Test
        public void verifyMsgsReceivedByHighLevel() {
            for (int i = 0; i < 4; i++)
                highlevelProbe.expectMsgEquals(convertLowLevelChars("H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                    "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                    "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                    "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                    "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                    "L|1<CR>"));
        }

        @Test
        public void testSuccessfulReceive() {
            new JavaTestKit(system) {{
                for (int i = 0; i < 4; i++) {
                    actorPair
                        .expect("<ENQ>")
                        .send("<ACK>")
                        .expect("<STX>0H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<STX>1P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<STX>2O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<STX>3R|1|^^^Org#|51^Strep Species|||N<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<STX>4R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<STX>5L|1<CR><ETX>{CS}<CR><LF>")
                        .send("<ACK>")
                        .expect("<EOT>");
                }
                for (int i = 0; i < 4; i++) {
                    actorPair
                        .send("<ENQ>")
                        .expect("<ACK>")
                        .send("<STX>0H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>")
                        .expect("<ACK>")
                        .send("<STX>1P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>")
                        .expect("<ACK>")
                        .send("<STX>2O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<ETX><CR><LF>")
                        .expect("<ACK>")
                        .send("<STX>3R|1|^^^Org#|51^Strep Species|||N<CR><ETX>{CS}<CR><LF>")
                        .expect("<ACK>")
                        .send("<STX>4R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR><ETX>{CS}<CR><LF>")
                        .expect("<ACK>")
                        .send("<STX>5L|1<CR><ETX>4A<CR><LF>")
                        .expect("<ACK>")
                        .send("<EOT>");
                }
            }};
        }

        private static class ActorPair {
            private final ActorRef underTest;
            private final JavaTestKit probe;

            private ActorPair(ActorRef underTest, JavaTestKit probe) {
                this.underTest = underTest;
                this.probe = probe;
            }

            public ActorPair send(String msg) {
                BytesMessage msgToSend = new BytesMessage(convertLowLevelChars(msg));
                underTest.tell(msgToSend, probe.getRef());
                return this;
            }

            public ActorPair expect(String msg) {
                BytesMessage expectedMsg = new BytesMessage(convertLowLevelChars(msg));
                probe.expectMsgEquals(expectedMsg);
                return this;
            }
        }
    }
}