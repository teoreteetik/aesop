package ee.lis.interfaces.lis2a2.record.field;

public class PrimitiveField extends Field {
    private final String data;

    public PrimitiveField(String data) {
        super(data);
        this.data = data;
    }

    @Override
    public String toString() {
        return "PrimitiveField{" +
            "data='" + data + '\'' +
            '}';
    }
}
