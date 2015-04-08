package ee.lis.interfaces.lis2a2.record;

import static java.util.stream.Collectors.toList;
import ee.lis.interfaces.lis2a2.DelimitedData;
import ee.lis.interfaces.lis2a2.record.field.Field;
import java.util.List;

public class Q extends LIS2A2Record {

    public static Q create(int sequenceNumber) {
        return (Q) new Q().setField(1, Type.Q.name()).setField(2, String.valueOf(sequenceNumber));
    }

    private Q() {
    }

    Q(DelimitedData<Field> data) {
        super(data);
    }

    public List<String> getSpecimenIds() {
        List<Field> idFields = getRepeats(3);
        return idFields.stream().map(f -> f.toComponentField().getComponent(2)).collect(toList());
    }

    public List<String> getAnalysisCodes() {
        List<Field> analysisFields = getRepeats(5);
        return analysisFields.stream().map(a -> a.toComponentField().getComponent(4)).collect(toList());
    }

    @Override
    protected LIS2A2Record getNew(DelimitedData<Field> data) {
        return new Q(data);
    }

    @Override
    public String toString() {
        return "Q{" +
            "data=" + data +
            '}';
    }
}