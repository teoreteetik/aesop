package ee.lis.util.template_engine;

public class SimpleEntity extends Entity<SimpleTemplate> {

    private String value;

    public SimpleEntity(SimpleTemplate spec) {
        super(spec);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value == null ? "" : value;
    }

    @Override
    public String toString() {
        return "SimpleEntity{" +
            "value='" + value + '\'' +
            '}';
    }
}