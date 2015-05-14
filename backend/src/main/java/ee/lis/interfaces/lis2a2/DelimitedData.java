package ee.lis.interfaces.lis2a2;

import static java.util.Collections.max;
import static java.util.Collections.unmodifiableMap;
import java.util.*;

public class DelimitedData<T extends HasStringRepresentation> {
    private final String delimiter;
    private final Map<Integer, T> fields;

    public DelimitedData(String delimiter) {
        this(delimiter, new HashMap<>());
    }

    public DelimitedData(String delimiter, Map<Integer, T> fields) {
        this.delimiter = delimiter;
        this.fields = unmodifiableMap(fields);
    }

    public DelimitedData<T> setField(int index, T value) {
        Map<Integer, T> fields = new HashMap<>(this.fields);
        fields.put(index, value);
        return new DelimitedData<T>(delimiter, fields);
    }

    public String asString() {
        int index = 0;
        List<String> stringValues = new ArrayList<>();
        Map<Integer, T> copy = new HashMap<>(this.fields);
        while(!copy.isEmpty()) {
            T value = copy.remove(index++);
            stringValues.add(value == null ? "" : value.asString());
        }
        return String.join(delimiter, stringValues);
    }

    public T get(int index) {
        return fields.get(index);
    }
    public int getSize() {
        return fields.isEmpty() ? 0 : (max(fields.keySet()) + 1);
    }

    @Override
    public String toString() {
        return "DelimitedData{" +
            "delimiter='" + delimiter + '\'' +
            ", fields=" + fields +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelimitedData<?> that = (DelimitedData<?>) o;

        if (!delimiter.equals(that.delimiter)) return false;
        return fields.equals(that.fields);

    }

    @Override
    public int hashCode() {
        int result = delimiter.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }
}
