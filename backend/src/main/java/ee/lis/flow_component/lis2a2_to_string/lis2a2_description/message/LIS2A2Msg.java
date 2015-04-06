package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.LIS2A2Record;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LIS2A2Msg implements Serializable {
    protected final List<LIS2A2Record> records;

    protected LIS2A2Msg(List<LIS2A2Record> records) {
        this.records = Collections.unmodifiableList(new ArrayList<>(records));
    }

    public String asString() {
        return this.records.stream().map(LIS2A2Record::asString).collect(Collectors.joining());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRecords(LIS2A2Record parentLevel, Class<T> recordType) {
        List<T> result = new ArrayList<>();
        for (int i = records.indexOf(parentLevel) + 1;
             i < records.size() && !parentLevel.getClass().equals(records.get(i).getClass());
             i++)
            if (records.get(i).getClass().equals(recordType))
                result.add((T) records.get(i));
        return result;
    }

    @Override
    public String toString() {
        return "LIS2A2Msg{" +
            "records=" + records +
            '}';
    }
}