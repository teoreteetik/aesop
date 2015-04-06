package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.LIS2A2Record;
import java.util.List;

public class LIS2A2OrderMsg extends LIS2A2Msg {
    public LIS2A2OrderMsg(List<LIS2A2Record> records) {
        super(records);
    }
}