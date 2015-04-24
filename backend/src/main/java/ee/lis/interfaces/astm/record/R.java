package ee.lis.interfaces.astm.record;

import ee.lis.interfaces.astm.DelimitedData;
import ee.lis.interfaces.astm.record.field.Field;

public class R extends AstmRecord {

    public static R create(int sequenceNumber) {
        return (R) new R().setField(1, Type.R.name()).setField(2, String.valueOf(sequenceNumber));
    }

    private R() {
    }

    R(DelimitedData<Field> data) {
        super(data);
    }

    public String getAnalysisCode() {
        return getComponentValue(3, 4);
    }

    public String getAnalysisName() {
        return getComponentValue(3, 2);
    }

    public String getResultValue() {
        return getFieldValue(4);
    }

    public String getUnit() {
        return getFieldValue(5);
    }

    @Override
    protected AstmRecord getNew(DelimitedData<Field> data) {
        return new R(data);
    }

    @Override
    public String toString() {
        return "R{" +
            "data=" + data +
            '}';
    }
}