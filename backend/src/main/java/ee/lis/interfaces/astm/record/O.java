package ee.lis.interfaces.astm.record;

import ee.lis.interfaces.astm.DelimitedData;
import ee.lis.interfaces.astm.record.field.CompomentField;
import ee.lis.interfaces.astm.record.field.Field;

public class O extends AstmRecord {

    public static O create(int sequenceNumber) {
        return (O) new O().setField(1, Type.O.name()).setField(2, String.valueOf(sequenceNumber));
    }

    private O() {
    }

    O(DelimitedData<Field> data) {
        super(data);
    }

    public O addAnalysis(String loinc, String name, String type, String code) {
        return (O) addRepeat(5, new CompomentField()
            .setComponent(1, loinc)
            .setComponent(2, name)
            .setComponent(3, type)
            .setComponent(4, code));
    }

    public O addPriorityCode(String priorityCode) {
        return (O) addRepeat(6, priorityCode);
    }

    public String getSpecimenId() {
        return getComponentValue(3, 1);
    }

    public O setSpecimenId(String specimenId) {
        return (O) setComponent(3, 1, specimenId);
    }

    @Override
    protected O getNew(DelimitedData<Field> data) {
        return new O(data);
    }

    @Override
    public String toString() {
        return "O{" +
            "data=" + data +
            '}';
    }
}
