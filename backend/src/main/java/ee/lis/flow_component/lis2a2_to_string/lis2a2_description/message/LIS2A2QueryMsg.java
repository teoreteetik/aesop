package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.LIS2A2Record;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.QueryRecord;
import java.util.List;
import java.util.stream.Collectors;

public class LIS2A2QueryMsg extends LIS2A2Msg {

    public LIS2A2QueryMsg(List<LIS2A2Record> records) {
            super(records);
        }

    public List<QueryRecord> getQueryRecords() {
        return records.stream()
            .filter(record -> record instanceof QueryRecord)
            .map(record -> (QueryRecord) record)
            .collect(Collectors.toList());
    }
}