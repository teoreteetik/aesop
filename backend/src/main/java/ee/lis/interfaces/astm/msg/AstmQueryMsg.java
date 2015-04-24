package ee.lis.interfaces.astm.msg;

import ee.lis.interfaces.astm.record.AstmRecord;
import ee.lis.interfaces.astm.record.Q;
import java.util.List;
import java.util.stream.Collectors;

public class AstmQueryMsg extends AstmMsg {

    public AstmQueryMsg(List<AstmRecord> records) {
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
        return "AstmQueryMsg{" +
            "records=" + records +
            '}';
    }
}