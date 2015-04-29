package ee.lis.interfaces.astm.record;

import ee.lis.interfaces.astm.DelimitedData;
import ee.lis.interfaces.astm.record.field.Field;

public class C extends LIS2A2Record {

    public static C create(int sequenceNumber) {
        return (C) new C().setField(1, Type.C.name())
                          .setField(2, String.valueOf(sequenceNumber));
    }

    private C() {
    }

    C(DelimitedData<Field> data) {
        super(data);
    }

    @Override
    protected C getNew(DelimitedData<Field> data) {
        return new C(data);
    }

    @Override
    public String toString() {
        return "C{" +
            "data=" + data +
            '}';
    }
}