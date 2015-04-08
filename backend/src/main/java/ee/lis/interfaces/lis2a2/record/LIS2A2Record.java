package ee.lis.interfaces.lis2a2.record;

import static ee.lis.util.LowLevelUtils.CR;
import ee.lis.interfaces.lis2a2.DelimitedData;
import ee.lis.interfaces.lis2a2.record.field.CompomentField;
import ee.lis.interfaces.lis2a2.record.field.Field;
import ee.lis.interfaces.lis2a2.record.field.PrimitiveField;
import ee.lis.interfaces.lis2a2.record.field.RepeatField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class LIS2A2Record {

    public static LIS2A2Record fromString(String record) {
        String crRemoved = record.substring(0, record.lastIndexOf(CR));
        String[] parts = crRemoved.split(Pattern.quote("|"));

        Map<Integer, Field> fields = new HashMap<>();
        for (int i = 0; i < parts.length; i++)
            fields.put(i, new Field(parts[i]));

        Type recordType = Type.valueOf(fields.get(0).toPrimitiveField().asString());
        DelimitedData<Field> data = new DelimitedData<Field>("|", fields);
        switch (recordType) {
            case H: return new H(data);
            case P: return new P(data);
            case O: return new O(data);
            case R: return new R(data);
            case Q: return new Q(data);
            case L: return new L(data);
            case C: return new C(data);
            default: throw new RuntimeException("Unexpected record type: " + recordType.name());
        }
    }

    protected enum Type {
        H, P, O, R, Q, L, C
    }

    protected final DelimitedData<Field> data;

    protected abstract LIS2A2Record getNew(DelimitedData<Field> data);

    protected LIS2A2Record(DelimitedData<Field> data) {
        this.data = data;
    }

    public LIS2A2Record() {
        this(new DelimitedData<>("|"));
    }

    public String asString() {
        return data.asString() + CR;
    }

    protected LIS2A2Record setField(int fieldIndex, Field field) {
        return getNew(data.setField(fieldIndex - 1, field));
    }

    protected LIS2A2Record setField(int fieldIndex, String value) {
        Field newField = new PrimitiveField(value);
        return setField(fieldIndex, newField);
    }

    protected LIS2A2Record setComponent(int fieldIndex, int componentIndex, Field value) {
        Field oldField = data.get(fieldIndex - 1);
        CompomentField newField = oldField == null ? new CompomentField().setComponent(componentIndex, value)
            : ((CompomentField) oldField).setComponent(componentIndex, value);
        return getNew(data.setField(fieldIndex - 1, newField));
    }

    protected LIS2A2Record setComponent(int fieldIndex, int componentIndex, String value) {
        return setComponent(fieldIndex, componentIndex, new PrimitiveField(value));
    }

    protected LIS2A2Record addRepeat(int fieldIndex, Field value) {
        Field oldField = data.get(fieldIndex - 1);
        RepeatField newField = oldField == null ? new RepeatField().addRepeat(value)
            : ((RepeatField) oldField).addRepeat(value);
        return getNew(data.setField(fieldIndex - 1, newField));
    }

    protected LIS2A2Record addRepeat(int fieldIndex, String value) {
        return addRepeat(fieldIndex, new PrimitiveField(value));
    }

    protected String getFieldValue(int fieldIndex) {
        return data.get(fieldIndex - 1).toPrimitiveField().asString();
    }

    protected String getComponentValue(int fieldIndex, int componentIndex) {
        return data.get(fieldIndex - 1).toComponentField().getComponent(componentIndex);
    }

    protected List<Field> getRepeats(int fieldIndex) {
        return data.get(fieldIndex - 1).asRepeatField().getRepeats();
    }
}