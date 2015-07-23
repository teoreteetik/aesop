package ee.lis.interfaces.lis2a2.msg;

import ee.lis.interfaces.lis2a2.record.LIS2A2Record;
import java.util.ArrayList;
import java.util.List;

public class LIS2A2OrderMsg extends LIS2A2Msg {

    public LIS2A2OrderMsg() {
        super(new ArrayList<>());
    }
    public LIS2A2OrderMsg(List<LIS2A2Record> records) {
        super(records);
    }

    public LIS2A2OrderMsg addRecord(LIS2A2Record record) {
        List<LIS2A2Record> newRecords = new ArrayList<>(this.records);
        newRecords.add(record);
        return new LIS2A2OrderMsg(newRecords);
    }

    @Override
    public String toString() {
        return "LIS2A2OrderMsg{" +
            "records=" + records +
            '}';
    }
}