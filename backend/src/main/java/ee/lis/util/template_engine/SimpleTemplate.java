package ee.lis.util.template_engine;

public class SimpleTemplate implements Template {

    @Override
    public SimpleEntity createEntity() {
        return new SimpleEntity(this);
    }

    @Override
    public SimpleEntity parseEntityFromString(String entityAsString) {
        SimpleEntity entity = new SimpleEntity(this);
        entity.setValue(entityAsString);
        return entity;
    }

    @Override
    public String toString() {
        return "SimpleTemplate{}";
    }
}