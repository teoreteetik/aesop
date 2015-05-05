package ee.lis.flow_component;

import static ee.lis.TestUtils.convertLowLevelChars;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.flow_component.astme138195.AstmE138195Actor;
import ee.lis.flow_component.astme138195.AstmE138195ActorConf;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AstmE138195ControllerTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testSuccessfulReceive() {
        new JavaTestKit(system) {{
            final JavaTestKit lowlevelProbe = new JavaTestKit(system);
            final JavaTestKit highlevelProbe = new JavaTestKit(system);

            final ActorRef astme138195Controller = system.actorOf(Props.create(AstmE138195Actor.class));
            astme138195Controller.tell(new AstmE138195ActorConf(240, lowlevelProbe.getRef(), highlevelProbe.getRef()), getRef());

            ActorPair actorPair = new ActorPair(astme138195Controller, lowlevelProbe);

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

            highlevelProbe.expectMsgEquals(convertLowLevelChars("H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                "L|1<CR>"));
        }};
    }

    @Test
    public void testSuccessfulSend() {
        new JavaTestKit(system) {{
            final JavaTestKit lowlevelProbe = new JavaTestKit(system);
            final JavaTestKit highlevelProbe = new JavaTestKit(system);

            final ActorRef astmE1381Actor = system.actorOf(Props.create(AstmE138195Actor.class));
            astmE1381Actor.tell(new AstmE138195ActorConf(240, lowlevelProbe.getRef(), highlevelProbe.getRef()), getRef());

            ActorPair actorPair = new ActorPair(astmE1381Actor, lowlevelProbe);

            astmE1381Actor.tell(convertLowLevelChars(
                "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                    "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                    "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                    "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                    "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                    "L|1<CR>"), getRef());

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