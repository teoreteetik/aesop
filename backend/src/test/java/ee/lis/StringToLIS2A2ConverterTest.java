package ee.lis;

import static ee.lis.TestUtils.convertLowLevelChars;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.flow_component.lis2a2_to_string.StringToLIS2A2Converter;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message.LIS2A2ResultMsg;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.OrderRecord;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.PatientRecord;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.ResultRecord;
import ee.lis.util.CommonProtocol.DestinationConf;
import java.util.List;
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
        new JavaTestKit(system) {{
            final ActorRef stringToLIS2A2Converter = system.actorOf(Props.create(StringToLIS2A2Converter.class));

            String resultMessage = convertLowLevelChars(
                "H|\\^&||Password1|Micro1|2937 Southwestern Avenue^Buffalo^NY^73205||319412-9722|LS|1||P|1394-94|19890501074500<CR>" +
                    "P|1||52483291||Smith^John|Samuels|19600401|M|W|4526 C Street^Fresno^CA^92304||(402)7823424X242|542^Dr. Brown|||72^in.|175^lb.||Penicillin||||19890428|IP|Ward1|||C|M|WSP||ER|PC^Prompt Care<CR>" +
                    "O|1|5762^01||^^^BC^BloodCulture^POSCOMBO|R|198905011530|198905020700|||456^Farnsworth|N|||198905021130|BL^Blood|123^Dr. Wirth||||||||Instrument#1|||ER|N<CR>" +
                    "R|1|^^^Org#|51^Strep Species|||N<CR>" +
                    "R|2|^^^Bio|BH+^Beta Hemolytic|||N<CR>" +
                    "L|1<CR>");

            final JavaTestKit destination = new JavaTestKit(system);
            stringToLIS2A2Converter.tell(new DestinationConf(destination.getRef()), getRef());
            stringToLIS2A2Converter.tell(resultMessage, getRef());
            LIS2A2ResultMsg received = destination.expectMsgClass(LIS2A2ResultMsg.class);
            List<PatientRecord> patientRecords = received.getPatientRecords();
            Assert.assertEquals(1, patientRecords.size());

            PatientRecord patientRecord = patientRecords.get(0);

            Assert.assertEquals("Smith", patientRecord.getSurname());
            Assert.assertEquals("John", patientRecord.getFirstName());
            Assert.assertEquals("52483291", patientRecord.getPatientId());

            List<OrderRecord> orderRecords = received.getOrderRecords(patientRecords.get(0));
            Assert.assertEquals(1, orderRecords.size());

            List<ResultRecord> resultRecords = received.getResultRecords(orderRecords.get(0));
            Assert.assertEquals(2, resultRecords.size());
        }};
    }
}
