package ee.lis.interfaces.astm.msg;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import ee.lis.interfaces.astm.record.LIS2A2Record;
import java.util.ArrayList;
import java.util.List;

public abstract class LIS2A2Msg {

    protected final List<LIS2A2Record> records;

    public LIS2A2Msg(List<LIS2A2Record> records) {
        this.records = unmodifiableList(records);
    }

    @SuppressWarnings("unchecked")
    public <T extends LIS2A2Record> List<T> getRecords(LIS2A2Record parentLevel, Class<T> recordType) {
        List<T> result = new ArrayList<>();
        for (int i = records.indexOf(parentLevel) + 1; i < records.size() && !parentLevel.getClass().equals(records.get(i).getClass()); i++)
            if (records.get(i).getClass().equals(recordType))
                result.add((T) records.get(i));
        return result;
    }

    public String asString() {
        return records.stream().map(LIS2A2Record::asString).collect(joining());
    }
}