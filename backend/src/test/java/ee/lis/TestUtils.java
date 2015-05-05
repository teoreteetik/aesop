package ee.lis;


import static ee.lis.util.LowLevelUtils.*;
import ee.lis.interfaces.MyLabMessages.*;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static String convertLowLevelChars(String string) {
        String result = string
            .replaceAll("<STX>", "" + STX)
            .replaceAll("<ENQ>", "" + ENQ)
            .replaceAll("<EOT>", "" + EOT)
            .replaceAll("<ACK>", "" + ACK)
            .replaceAll("<ETX>", "" + ETX)
            .replaceAll("<CR>", "" + CR)
            .replaceAll("<LF>", "" + LF);
        return result.replaceAll("\\{CS\\}", getCheckSum(result));
    }

    public static Order Order(Patient patient, List<Container> containers) {
        return new Order(patient, containers);
    }

    public static Result Result(String value, String unit) {
        return new Result(value, unit);
    }

    public static List<Analysis> analyses(Analysis... analyses) {
        return Arrays.asList(analyses);
    }

    public static Analysis Analysis(String code, String name, Result result) {
        return new Analysis(code, name, result);
    }

    public static Patient Patient(String firstName, String surname, String patientId) {
        return new Patient(firstName, surname, patientId);
    }

    public static Container Container(String specimenId, List<Analysis> analyses) {
        return new Container(specimenId, analyses);
    }

    public static MyLabResultMsg MyLabResultMsg(Order order) {
        return new MyLabResultMsg(order);
    }

    public static List<Container> containers(Container... containers) {
        return Arrays.asList(containers);
    }

    public static MyLabQueryMsg MyLabQueryMsg(List<String> specimenIds, List<String> analysisCodes) {
        return new MyLabQueryMsg(specimenIds, analysisCodes);
    }

    public static MyLabOrderMsg MyLabOrderMsg(Order order) {
        return new MyLabOrderMsg(order);
    }
}
