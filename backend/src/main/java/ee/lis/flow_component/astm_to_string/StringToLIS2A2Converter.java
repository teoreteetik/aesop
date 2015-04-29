package ee.lis.flow_component.astm_to_string;

import static ee.lis.util.LowLevelUtils.CR;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.interfaces.astm.msg.*;
import ee.lis.interfaces.astm.msg.LIS2A2Msg;
import ee.lis.interfaces.astm.msg.LIS2A2QueryMsg;
import ee.lis.interfaces.astm.record.LIS2A2Record;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.Q;
import ee.lis.interfaces.astm.record.R;
import ee.lis.util.CommonProtocol.RecipientConf;
import java.util.ArrayList;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class StringToLIS2A2Converter extends FlowComponent<RecipientConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(String.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(String msgAsString) {
        LIS2A2Msg LIS2A2Msg = stringMsgToAstmMsg(msgAsString);
        conf.recipient.tell(LIS2A2Msg, self());
    }

    public LIS2A2Msg stringMsgToAstmMsg(String msgAsString) {
        List<LIS2A2Record> records = new ArrayList<>();
        String[] recordLines = msgAsString.split("(?<=" + CR + ")");
        for (String record : recordLines) {
            LIS2A2Record lis2a2Record = LIS2A2Record.fromString(record);
            records.add(lis2a2Record);
        }

        Class<? extends LIS2A2Msg> msgType = getMsgType(records);
        if (msgType.equals(LIS2A2OrderMsg.class))
            return new LIS2A2OrderMsg(records);
        else if (msgType.equals(LIS2A2ResultMsg.class))
            return new LIS2A2ResultMsg(records);
        else if (msgType.equals(LIS2A2QueryMsg.class))
            return new LIS2A2QueryMsg(records);
        else
            throw new RuntimeException("Unexpected message type");
    }

    public Class<? extends LIS2A2Msg> getMsgType(List<LIS2A2Record> records) {
        Class<? extends LIS2A2Msg> msgType = null;
        for (LIS2A2Record record : records)
            if (record instanceof O)
                msgType = LIS2A2OrderMsg.class;
            else if (record instanceof Q)
                msgType = LIS2A2QueryMsg.class;
            else if (record instanceof R)
                msgType = LIS2A2ResultMsg.class;
        return msgType;
    }
}