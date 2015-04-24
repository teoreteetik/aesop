package ee.lis.interfaces.astm.record.field;

import ee.lis.interfaces.astm.DelimitedData;

public class CompomentField extends Field {
    private final DelimitedData<Field> data;

    public CompomentField() {
        this(new DelimitedData<>("^"));
    }

    private CompomentField(DelimitedData<Field> data) {
        super(data.asString());
        this.data = data;
    }

    public CompomentField setComponent(int index, String value) {
        return setComponent(index, new PrimitiveField(value));
    }

    public CompomentField setComponent(int index, Field value) {
        return new CompomentField(data.setField(index - 1, value));
    }

    public String getComponent(int index) {
        return data.get(index - 1).toPrimitiveField().asString();
    }

    @Override
    public String toString() {
        return "CompomentField{" +
            "data=" + data +
            '}';
    }
}