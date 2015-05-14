package ee.lis.interfaces.lis2a2.record;

import ee.lis.interfaces.lis2a2.DelimitedData;
import ee.lis.interfaces.lis2a2.record.field.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class H extends LIS2A2Record {

    public static H create() {
        return (H) new H().setField(1, Type.H.name())
                          .setField(2, "\\^&");
    }

    private H() {
    }

    H(DelimitedData<Field> data) {
        super(data);
    }

    public H setMessageControlId(String messageControlId) {
        return (H) setField(3, messageControlId);
    }

    public H setSenderId(String senderId) {
        return (H) setField(5, senderId);
    }


    public H setSenderAdress(String streetAddress, String city, String state, String zip, String countryCode) {
        return (H) setComponent(6, 1, streetAddress)
            .setComponent(6, 2, city)
            .setComponent(6, 3, state)
            .setComponent(6, 4, zip)
            .setComponent(6, 5, countryCode);
    }

    public H setReceiverId(String receiverId) {
        return (H) setField(10, receiverId);
    }

    public H setProcessingId(String processingId) {
        return (H) setField(12, processingId);
    }

    public H setVersionNumber(String versionNumber) {
        return (H) setField(13, versionNumber);
    }

    public H setDateTime(Date dateTime) {
        return (H) setField(14, new SimpleDateFormat("yyyyMMddHHmmss").format(dateTime));
    }

    @Override
    protected H getNew(DelimitedData<Field> fields) {
        return new H(fields);
    }

    @Override
    public String toString() {
        return "H{" +
            "data=" + data +
            '}';
    }
}