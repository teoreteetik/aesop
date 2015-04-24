package ee.lis.interfaces.astm.msg;

import ee.lis.interfaces.astm.record.AstmRecord;
import java.util.ArrayList;
import java.util.List;

public class AstmOrderMsg extends AstmMsg {

    public AstmOrderMsg() {
        super(new ArrayList<>());
    }
    public AstmOrderMsg(List<AstmRecord> records) {
        super(records);
    }

    public AstmOrderMsg addRecord(AstmRecord record) {
        List<AstmRecord> newRecords = new ArrayList<>(this.records);
        newRecords.add(record);
        return new AstmOrderMsg(newRecords);
    }

    @Override
    public String toString() {
        return "AstmOrderMsg{" +
            "records=" + records +
            '}';
    }
}