package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;
import ee.lis.util.template_engine.CompositeTemplate;
import ee.lis.util.template_engine.SimpleEntity;
import ee.lis.util.template_engine.SimpleTemplate;

public class OrderRecord extends LIS2A2Record {
    public OrderRecord(CompositeEntity entity) {
        super(entity);
    }

    public OrderRecord() {
        super(LIS2A2RecordType.O.template.createEntity());
    }

    public OrderRecord setSequenceNumber(int sequenceNumber) {
        entity.simple("O.2").setValue(String.valueOf(sequenceNumber));
        return this;
    }

    public OrderRecord addAnalysis(String loinc, String name, String type, String code) {
        CompositeEntity analysisEntity = entity.repeat("O.5").getRepeatTemplate(CompositeTemplate.class).createEntity();
        analysisEntity.simple("LOINC").setValue(loinc);
        analysisEntity.simple("NAME").setValue(name);
        analysisEntity.simple("TYPE").setValue(type);
        analysisEntity.simple("LOCAL_CODE").setValue(code);
        entity.repeat("O.5").addRepeat(analysisEntity);
        return this;
    }

    public OrderRecord addPriorityCode(String priorityCode) {
        SimpleEntity p = entity.repeat("O.6").getRepeatTemplate(SimpleTemplate.class).createEntity();
        p.setValue(priorityCode);
        entity.repeat("O.6").addRepeat(p);
        return this;
    }

    public String getSpecimenId() {
        return entity.comp("O.3").simple("SPECIMEN_ID").asString();
    }
    public OrderRecord setSpecimenId(String specimenId) {
        entity.comp("O.3").simple("SPECIMEN_ID").setValue(specimenId);
        return this;
    }
}