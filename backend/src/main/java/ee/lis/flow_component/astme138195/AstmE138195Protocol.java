package ee.lis.flow_component.astme138195;

import static ee.lis.util.LowLevelUtils.*;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;

public class AstmE138195Protocol {
    public static final BytesMessage ACKBytes = new BytesMessage((byte) ACK);
    public static final BytesMessage NAKBytes = new BytesMessage((byte) NAK);
    public static final BytesMessage EOTBytes = new BytesMessage((byte) EOT);
    public static final BytesMessage ENQBytes = new BytesMessage((byte) ENQ);
}