package ee.lis.interfaces.astm.record.field;

import ee.lis.interfaces.astm.DelimitedData;
import java.util.ArrayList;
import java.util.List;

public class RepeatField extends Field {
    private final DelimitedData<Field> data;

    public RepeatField() {
        this(new DelimitedData<>("\\"));
    }

    private RepeatField(DelimitedData<Field> data) {
        super(data.asString());
        this.data = data;
    }

    public RepeatField addRepeat(Field field) {
        return new RepeatField(data.setField(data.getSize(), field));
    }

    public List<Field> getRepeats() {
        List<Field> result = new ArrayList<>();
        for (int i = 0; i < data.getSize(); i++)
            result.add(data.get(i));
        return result;
    }

    @Override
    public String toString() {
        return "RepeatField{" +
            "data=" + data +
            '}';
    }
}
