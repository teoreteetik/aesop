package ee.lis.interfaces.astm.record;

import ee.lis.interfaces.astm.DelimitedData;
import ee.lis.interfaces.astm.record.field.Field;

public class P extends LIS2A2Record {

    public static P create(int sequenceNumber) {
        return (P) new P().setField(1, Type.P.name()).setField(2, String.valueOf(sequenceNumber));
    }

    private P() {
    }

    P(DelimitedData<Field> fields) {
        super(fields);
    }

    public String getFirstName() {
        return getComponentValue(6, 2);
    }

    public P setFirstName(String firstName) {
        return (P) setComponent(6, 2, firstName);
    }

    public String getSurname() {
        return getComponentValue(6, 1);
    }

    public P setSurname(String surname) {
        return (P) setComponent(6, 1, surname);
    }

    public String getPatientId() {
        return getFieldValue(4);
    }

    public P setPatientId(String patientId) {
        return (P) setField(4, patientId);
    }

    @Override
    protected P getNew(DelimitedData<Field> fields) {
        return new P(fields);
    }

    @Override
    public String toString() {
        return "P{" +
            "data=" + data +
            '}';
    }
}
