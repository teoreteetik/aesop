package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;

public class TerminatorRecord extends LIS2A2Record {
    public TerminatorRecord(CompositeEntity entity) {
        super(entity);
    }
    public TerminatorRecord() {
        super(LIS2A2RecordType.L.template.createEntity());
    }
    public TerminatorRecord setSequenceNumber(int sequenceNumber) {
        entity.simple("L.2").setValue(String.valueOf(sequenceNumber));
        return this;
    }
}