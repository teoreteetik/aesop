package ee.lis.interfaces.lis2a2.msg;

import ee.lis.interfaces.lis2a2.record.LIS2A2Record;
import ee.lis.interfaces.lis2a2.record.Q;
import java.util.List;
import java.util.stream.Collectors;

public class LIS2A2QueryMsg extends LIS2A2Msg {

    public LIS2A2QueryMsg(List<LIS2A2Record> records) {
        super(records);
    }

    public List<Q> getQueryRecords() {
        return records.stream()
            .filter(record -> record instanceof Q)
            .map(record -> (Q) record)
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "LIS2A2QueryMsg{" +
            "records=" + records +
            '}';
    }
}