package ee.lis.interfaces.astm.msg;

import ee.lis.interfaces.astm.record.LIS2A2Record;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.P;
import ee.lis.interfaces.astm.record.R;
import java.util.List;
import java.util.stream.Collectors;

public class LIS2A2ResultMsg extends LIS2A2Msg {
    public LIS2A2ResultMsg(List<LIS2A2Record> records) {
        super(records);
    }

    public List<P> getPatientRecords() {
        return records.stream()
            .filter(record -> record instanceof P)
            .map(record -> (P) record)
            .collect(Collectors.toList());
    }

    public List<O> getOrderRecords(P patientRecord) {
        return getRecords(patientRecord, O.class);
    }

    public List<R> getResultRecords(O orderRecord) {
        return getRecords(orderRecord, R.class);
    }

    @Override
    public String toString() {
        return "LIS2A2ResultMsg{" +
            "records=" + records +
            '}';
    }
}