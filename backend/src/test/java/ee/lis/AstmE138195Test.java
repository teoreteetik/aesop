package ee.lis;

import static ee.lis.TestUtils.convertLowLevelChars;
import static ee.lis.flow_component.astme138195.AstmE138195Protocol.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.flow_component.astme138195.AstmE138195Actor;
import ee.lis.flow_component.astme138195.AstmE138195ActorConf;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AstmE138195Test {

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
            final JavaTestKit socketActer = new JavaTestKit(system);
            final JavaTestKit highLevelActor = new JavaTestKit(system);

            final ActorRef lowLevelActor = system.actorOf(Props.create(AstmE138195Actor.class));
            lowLevelActor.tell(new AstmE138195ActorConf(240, socketActer.getRef(), highLevelActor.getRef()), getRef());

            lowLevelActor.tell(ENQBytes, getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<ETX><CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>R|1|^^^Org#|51^Strep Species|||N<CR><ETX>{CS}<CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR><ETX>{CS}<CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(new BytesMessage(convertLowLevelChars(
                "<STX>L|1<CR><ETX>4A<CR><LF>")), getRef());
            socketActer.expectMsgEquals(ACKBytes);

            lowLevelActor.tell(EOTBytes, getRef());
            highLevelActor.expectMsgEquals(convertLowLevelChars("H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
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
            final JavaTestKit socketActer = new JavaTestKit(system);
            final JavaTestKit highLevelActor = new JavaTestKit(system);

            final ActorRef astmE1381Actor = system.actorOf(Props.create(AstmE138195Actor.class));
            astmE1381Actor.tell(new AstmE138195ActorConf(240, socketActer.getRef(), highLevelActor.getRef()), getRef());

            astmE1381Actor.tell(convertLowLevelChars(
                "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                    "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                    "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                    "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                    "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                    "L|1<CR>"), getRef());

            socketActer.expectMsgEquals(ENQBytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            BytesMessage msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>R|1|^^^Org#|51^Strep Species|||N<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            msg = socketActer.expectMsgClass(BytesMessage.class);
            assertListEqualsArray(convertLowLevelChars("<STX>L|1<CR><ETX>{CS}<CR><LF>").getBytes(), msg.bytes);
            astmE1381Actor.tell(ACKBytes, getRef());

            socketActer.expectMsgEquals(EOTBytes);
        }};
    }

    private void assertListEqualsArray(byte[] byteArray, List<Byte> byteList) {
        List<Byte> arrayToList = new ArrayList<>();
        for (byte b : byteArray)
            arrayToList.add(b);
        Assert.assertEquals(arrayToList, byteList);
    }
}