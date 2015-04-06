package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;
import java.util.Date;

public class HeaderRecord extends LIS2A2Record {
    public HeaderRecord(CompositeEntity entity) {
        super(entity);
    }

    public HeaderRecord() {
        super(LIS2A2RecordType.H.template.createEntity());
    }

    public HeaderRecord setMessageControlId(String messageControlId) {
        entity.simple("H.3").setValue(messageControlId);
        return this;
    }

    public HeaderRecord setSenderId(String senderId) {
        entity.simple("H.5").setValue(senderId);
        return this;
    }

    public HeaderRecord setSenderAdress(String streetAddress, String city, String state, String zip, String countryCode) {
        CompositeEntity addressField = entity.comp("H.6");
        addressField.simple("STREET_ADDRESS").setValue(streetAddress);
        addressField.simple("CITY").setValue(city);
        addressField.simple("STATE").setValue(state);
        addressField.simple("ZIP").setValue(zip);
        addressField.simple("COUNTRY_CODE").setValue(countryCode);
        return this;
    }

    public HeaderRecord setReceiverId(String receiverId) {
        entity.simple("H.10").setValue(receiverId);
        return this;
    }

    public HeaderRecord setProcessingId(String processingId) {
        entity.simple("H.12").setValue(processingId);
        return this;
    }

    public HeaderRecord setVersionNumber(String versionNumber) {
        entity.simple("H.13").setValue(versionNumber);
        return this;
    }

    public HeaderRecord setDateTime(Date dateTime) {
        entity.date("H.14").setValue(dateTime);
        return this;
    }


}