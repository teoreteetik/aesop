package ee.lis.interfaces.astm.msg;

import ee.lis.interfaces.astm.record.AstmRecord;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.P;
import ee.lis.interfaces.astm.record.R;
import java.util.List;
import java.util.stream.Collectors;

public class AstmResultMsg extends AstmMsg {
    public AstmResultMsg(List<AstmRecord> records) {
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
        return "AstmResultMsg{" +
            "records=" + records +
            '}';
    }
}