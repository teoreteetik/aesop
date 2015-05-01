package ee.lis.flow_component.astme138195;

import static ee.lis.flow_component.astme138195.AstmE138195Protocol.*;
import static ee.lis.util.LowLevelUtils.*;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class AstmE138195Actor extends FlowComponent<AstmE138195ActorConf> {

    private PartialFunction<Object, BoxedUnit> idleState;
    private PartialFunction<Object, BoxedUnit> receivingState;
    private PartialFunction<Object, BoxedUnit> sendingState;

    private final List<String> msgBeingSent = new ArrayList<>();
    private int sendingFrameNumber;
    private final StringBuilder msgBeingReceived = new StringBuilder();

    @Override
    public PartialFunction<Object, BoxedUnit> getBehaviour() {
        return idleState;
    }

    private AstmE138195Actor() {
        idleState = getIdleState();
        receivingState = getReceivingState();
        sendingState = getSendingState();
    }

    private PartialFunction<Object, BoxedUnit> getIdleState() {
        return ReceiveBuilder
            .match(String.class,
                this::goToSendingState)
            .matchEquals(ENQBytes, __ ->
                goToReceivingState())
            .build();
    }

    private void goToSendingState(String msgToSend) {
        sendingFrameNumber = 0;
        msgBeingSent.clear();
        Collections.addAll(msgBeingSent, msgToSend.split("(?<=" + CR + ")"));
        conf.lowLevelRecipient.tell(ENQBytes, self());
        context().become(sendingState);
    }

    private void goToReceivingState() {
        msgBeingReceived.setLength(0);
        conf.lowLevelRecipient.tell(ACKBytes, self());
        context().become(receivingState);
    }

    private PartialFunction<Object, BoxedUnit> getSendingState() {
        return ReceiveBuilder
            .matchEquals(ACKBytes, __ -> {
                if (msgBeingSent.isEmpty()) {
                    conf.lowLevelRecipient.tell(EOTBytes, self());
                    goToIdleState();
                } else {
                    sendNextFrameInBuffer();
                }
            })
            .matchAny(__ -> stash())
            .build();
    }

    private PartialFunction<Object, BoxedUnit> getReceivingState() {
        return ReceiveBuilder
            .matchEquals(EOTBytes, __ -> {
                conf.highLevelRecipient.tell(msgBeingReceived.toString(), self());
                msgBeingReceived.setLength(0);
                goToIdleState();
            })
            .match(BytesMessage.class, bytesMessage -> {
                msgBeingReceived.append(extractPayload(bytesMessage.bytes));
                conf.lowLevelRecipient.tell(ACKBytes, self());
            })
            .matchAny(__ -> stash())
            .build();
    }

    private void goToIdleState() {
        unstashAll();
        context().become(idleState);
    }

    private String extractPayload(List<Byte> frameBytes) {
        int index = frameBytes.indexOf((byte) ETX);
        if (index == -1)
            index = frameBytes.indexOf((byte) ETB);
        List<Byte> payloadBytes = frameBytes.subList(2, index);
        byte[] bytes = new byte[payloadBytes.size()];
        IntStream.range(0, payloadBytes.size()).forEach(i -> bytes[i] = payloadBytes.get(i));
        return new String(bytes);
    }

    private void sendNextFrameInBuffer() {
        String nextFrame = msgBeingSent.remove(0);
        if (nextFrame.length() > conf.maxFrameSize) {
            String partToSend = nextFrame.substring(0, conf.maxFrameSize);
            String remainingPart = nextFrame.substring(conf.maxFrameSize);
            msgBeingSent.add(0, remainingPart);
            conf.lowLevelRecipient.tell(getFrame(partToSend, ETB), self());
        } else {
            conf.lowLevelRecipient.tell(getFrame(nextFrame, ETX), self());
        }
        sendingFrameNumber = (sendingFrameNumber + 1) % 8;
    }

    private BytesMessage getFrame(String payload, char payloadDelimiter) {
        String frameForChecksum = "" + STX + sendingFrameNumber + payload + payloadDelimiter;
        String msg = frameForChecksum + getCheckSum(frameForChecksum) + CR + LF;
        return new BytesMessage(msg);
    }
}