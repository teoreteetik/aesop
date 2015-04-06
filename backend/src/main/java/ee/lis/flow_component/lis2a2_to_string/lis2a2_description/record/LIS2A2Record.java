package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;

public class LIS2A2Record {

    final CompositeEntity entity;

    protected LIS2A2Record(CompositeEntity entity) {
        this.entity = entity;
    }

    public String asString() {
        return entity.asString();
    }

    public CompositeEntity getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "LIS2A2Record{" +
            "entity=" + entity +
            '}';
    }
}
