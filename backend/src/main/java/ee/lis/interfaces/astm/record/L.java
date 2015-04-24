package ee.lis.interfaces.astm.record;

import ee.lis.interfaces.astm.DelimitedData;
import ee.lis.interfaces.astm.record.field.Field;

public class L extends AstmRecord {

    public static L create(int sequenceNumber) {
        return (L) new L().setField(1, Type.L.name()).setField(2, String.valueOf(sequenceNumber));
    }

    private L() {
    }

    L(DelimitedData<Field> data) {
        super(data);
    }

    @Override
    protected AstmRecord getNew(DelimitedData<Field> data) {
        return new L(data);
    }

    @Override
    public String toString() {
        return "L{" +
            "data=" + data +
            '}';
    }
}
