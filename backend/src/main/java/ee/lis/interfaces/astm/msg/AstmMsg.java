package ee.lis.interfaces.astm.msg;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import ee.lis.interfaces.astm.record.AstmRecord;
import java.util.ArrayList;
import java.util.List;

public abstract class AstmMsg {

    protected final List<AstmRecord> records;

    public AstmMsg(List<AstmRecord> records) {
        this.records = unmodifiableList(records);
    }

    @SuppressWarnings("unchecked")
    public <T extends AstmRecord> List<T> getRecords(AstmRecord parentLevel, Class<T> recordType) {
        List<T> result = new ArrayList<>();
        for (int i = records.indexOf(parentLevel) + 1; i < records.size() && !parentLevel.getClass().equals(records.get(i).getClass()); i++)
            if (records.get(i).getClass().equals(recordType))
                result.add((T) records.get(i));
        return result;
    }

    public String asString() {
        return records.stream().map(AstmRecord::asString).collect(joining());
    }
}