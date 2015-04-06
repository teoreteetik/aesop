package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.*;
import java.util.List;
import java.util.stream.Collectors;

public class LIS2A2ResultMsg extends LIS2A2Msg {
    public LIS2A2ResultMsg(List<LIS2A2Record> records) {
        super(records);
    }

    public List<PatientRecord> getPatientRecords() {
        return records.stream()
            .filter(record -> record instanceof PatientRecord)
            .map(record -> (PatientRecord) record)
            .collect(Collectors.toList());
    }

    public List<OrderRecord> getOrderRecords(PatientRecord patientRecord) {
        return getRecords(patientRecord, OrderRecord.class);
    }

    public List<ResultRecord> getResultRecords(OrderRecord orderRecord) {
        return getRecords(orderRecord, ResultRecord.class);
    }

    public List<CommentRecord> getCommentRecords(LIS2A2Record record) {
        return getRecords(record, CommentRecord.class);
    }
}