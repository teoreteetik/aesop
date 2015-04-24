package ee.lis.interfaces.astm.record.field;

import ee.lis.interfaces.astm.HasStringRepresentation;
import java.util.regex.Pattern;

public class Field implements HasStringRepresentation {
    private final String rawData;

    public Field(String rawData) {
        this.rawData = rawData;
    }

    public PrimitiveField toPrimitiveField() {
        return new PrimitiveField(rawData);
    }

    public CompomentField toComponentField() {
        CompomentField result = new CompomentField();
        String[] parts = rawData.split(Pattern.quote("^"));
        for (int i = 0; i < parts.length; i++)
            result = result.setComponent(i + 1, new Field(parts[i]));
        return result;
    }

    public RepeatField asRepeatField() {
        RepeatField result = new RepeatField();
        String parts[] = rawData.split(Pattern.quote("\\"));
        for (String part : parts) {
            result = result.addRepeat(new Field(part));}
        return result;
    }

    @Override
    public String asString() {
        return rawData == null ? "" : rawData;
    }

    @Override
    public String toString() {
        return "Field{" +
            "rawData='" + rawData + '\'' +
            '}';
    }
}
