package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;

public class PatientRecord extends LIS2A2Record {
    public PatientRecord(CompositeEntity entity) {
        super(entity);
    }

    public PatientRecord() {
        super(LIS2A2RecordType.P.template.createEntity());
    }

    public String getFirstName() {
        return entity.comp("P.6").simple("FIRST_NAME").asString();
    }

    public PatientRecord setFirstName(String firstName) {
        entity.comp("P.6").simple("FIRST_NAME").setValue(firstName);
        return this;
    }

    public String getSurname() {
        return entity.comp("P.6").simple("SURNAME").asString();
    }

    public PatientRecord setSurname(String surname) {
        entity.comp("P.6").simple("SURNAME").setValue(surname);
        return this;
    }

    public String getPatientId() {
        return entity.simple("P.4").asString();
    }

    public PatientRecord setPatientId(String patientId) {
        entity.simple("P.4").setValue(patientId);
        return this;
    }

    public PatientRecord setSequenceNumber(int sequenceNumber) {
        entity.simple("P.2").setValue(String.valueOf(sequenceNumber));
        return this;
    }
}
