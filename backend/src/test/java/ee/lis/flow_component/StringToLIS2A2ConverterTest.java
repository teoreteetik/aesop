package ee.lis.flow_component;

import static ee.lis.TestUtils.convertLowLevelChars;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.core.RecipientConf;
import ee.lis.flow_component.astm_to_string.StringToLIS2A2Converter;
import ee.lis.interfaces.astm.msg.LIS2A2ResultMsg;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringToLIS2A2ConverterTest {

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
    public void resultStringToAstmResultMessageTest() {
        new JavaTestKit(system) {{
            final ActorRef stringToAstmConverter = system.actorOf(Props.create(StringToLIS2A2Converter.class));

            String resultMessage = convertLowLevelChars(
                "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                    "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                    "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                    "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                    "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                    "L|1<CR>");

            final JavaTestKit probe = new JavaTestKit(system);
            stringToAstmConverter.tell(new RecipientConf(probe.getRef()), getRef());

            stringToAstmConverter.tell(resultMessage, getRef());

            LIS2A2ResultMsg received = probe.expectMsgClass(LIS2A2ResultMsg.class);
            Assert.assertEquals(resultMessage, received.asString());
        }};
    }
}
