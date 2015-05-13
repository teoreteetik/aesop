package ee.lis.flow_component;

import static akka.actor.ActorRef.noSender;
import static ee.lis.util.LowLevelUtils.CR;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.core.RecipientConf;
import ee.lis.flow_component.astm_to_string.StringToLIS2A2Converter;
import ee.lis.interfaces.astm.msg.LIS2A2ResultMsg;
import ee.lis.interfaces.astm.record.*;
import ee.lis.interfaces.astm.record.field.Field;
import ee.lis.interfaces.astm.record.field.PrimitiveField;
import java.util.ArrayList;
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
    public void resultStringToLIS2A2ResultMessageTest() {
        final ActorRef stringToLIS2A2Converter = system.actorOf(Props.create(StringToLIS2A2Converter.class));
        final JavaTestKit probe = new JavaTestKit(system);
        stringToLIS2A2Converter.tell(new RecipientConf(probe.getRef()), noSender());

        String inputString =
            "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500" + CR +
                "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care" + CR +
                "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N" + CR +
                "R|1|^^^Org#|51^Strep Species|||N" + CR +
                "R|2|^^^Bio|BH+^Beta Hemolytic|||N" + CR +
                "L|1" + CR;
        LIS2A2ResultMsg expectedOutput = new LIS2A2ResultMsg(new ArrayList<LIS2A2Record>() {{
            add(H.create()
                .setField(3, "")
                .setField(4, "Password1")
                .setField(5, "Micro1")
                .setField(6, "2937 Southwestern Avenue^Buffalo^NY^73205")
                .setField(7, "")
                .setField(8, "319412-9722")
                .setField(9, "LS")
                .setField(10, "1")
                .setField(11, "")
                .setField(12, "P")
                .setField(13, "1394-94")
                .setField(14, "19890501074500"));
            add(P.create(1)
                .setField(3, "")
                .setField(4, "52483291")
                .setField(5, "")
                .setField(6, "Smith^John")
                .setField(7, "Samuels")
                .setField(8, "19600401")
                .setField(9, "M")
                .setField(10, "W")
                .setField(11, "4526 C Street^Fresno^CA^92304")
                .setField(12, "")
                .setField(13, "(402)7823424X242")
                .setField(14, "542^Dr. Brown")
                .setField(15, "")
                .setField(16, "")
                .setField(17, "72^in.")
                .setField(18, "175^lb.")
                .setField(19, "")
                .setField(20, "Penicillin")
                .setField(21, "")
                .setField(22, "")
                .setField(23, "")
                .setField(24, "19890428")
                .setField(25, "IP")
                .setField(26, "Ward1")
                .setField(27, "")
                .setField(28, "")
                .setField(29, "C")
                .setField(30, "M")
                .setField(31, "WSP")
                .setField(32, "")
                .setField(33, "ER")
                .setField(34, "PC^Prompt Care"));
            add(O.create(1)
                .setField(3, "5762^01")
                .setField(4, "")
                .setField(5, "^^^BC^BloodCulture^POSCOMBO")
                .setField(6, "R")
                .setField(7, "198905011530")
                .setField(8, "198905020700")
                .setField(9, "")
                .setField(10, "")
                .setField(11, "456^Farnsworth")
                .setField(12, "N")
                .setField(13, "")
                .setField(14, "")
                .setField(15, "198905021130")
                .setField(16, "BL^Blood")
                .setField(17, "123^Dr. Wirth")
                .setField(18, "")
                .setField(19, "")
                .setField(20, "")
                .setField(21, "")
                .setField(22, "")
                .setField(23, "")
                .setField(24, "")
                .setField(25, "Instrument#1")
                .setField(26, "")
                .setField(27, "")
                .setField(28, "ER")
                .setField(29, "N"));
            add(R.create(1)
                .setField(3, "^^^Org#")
                .setField(4, "51^Strep Species")
                .setField(5, "")
                .setField(6, "")
                .setField(7, "N"));
            add(R.create(2)
                .setField(3, "^^^Bio")
                .setField(4, "BH+^Beta Hemolytic")
                .setField(5, "")
                .setField(6, "")
                .setField(7, "N"));
            add(L.create(1));
        }});

        stringToLIS2A2Converter.tell(inputString, noSender());

        LIS2A2ResultMsg received = probe.expectMsgClass(LIS2A2ResultMsg.class);

        Assert.assertEquals(expectedOutput, received);
        Assert.assertEquals(inputString, received.asString());
    }
}
