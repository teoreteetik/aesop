package ee.lis.flow_component.socket;

import ee.lis.util.LowLevelUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class SocketProtocol {

    public static class BytesMessage implements Serializable {
        public final List<Byte> bytes;

        public BytesMessage(byte[] bytes) {
            List<Byte> tmp = new ArrayList<>();
            for (byte b : bytes)
                tmp.add(b);
            this.bytes = Collections.unmodifiableList(tmp);
        }

        public BytesMessage(byte b) {
            this.bytes = Collections.singletonList(b);
        }

        public BytesMessage(String string) {
            List<Byte> bytes = new ArrayList<>();
            for (byte b : string.getBytes())
                bytes.add(b);
            this.bytes = Collections.unmodifiableList(bytes);
        }

        @Override
        public String toString() {
            return LowLevelUtils.formatToHumanReadable(new String(getByteArray()));
        }

        public byte[] getByteArray() {
            byte[] byteArray = new byte[bytes.size()];
            IntStream.range(0, bytes.size()).forEach(i -> byteArray[i] = bytes.get(i));
            return byteArray;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BytesMessage that = (BytesMessage) o;
            return bytes.equals(that.bytes);

        }

        @Override
        public int hashCode() {
            return bytes.hashCode();
        }
    }
}
