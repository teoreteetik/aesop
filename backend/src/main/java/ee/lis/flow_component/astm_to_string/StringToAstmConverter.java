package ee.lis.flow_component.astm_to_string;

import static ee.lis.util.LowLevelUtils.CR;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.interfaces.astm.msg.*;
import ee.lis.interfaces.astm.msg.AstmMsg;
import ee.lis.interfaces.astm.msg.AstmQueryMsg;
import ee.lis.interfaces.astm.record.AstmRecord;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.Q;
import ee.lis.interfaces.astm.record.R;
import ee.lis.util.CommonProtocol.RecipientConf;
import java.util.ArrayList;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class StringToAstmConverter extends FlowComponent<RecipientConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(String.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(String msgAsString) {
        AstmMsg astmMsg = stringMsgToAstmMsg(msgAsString);
        conf.recipient.tell(astmMsg, self());
    }

    public AstmMsg stringMsgToAstmMsg(String msgAsString) {
        List<AstmRecord> records = new ArrayList<>();
        String[] recordLines = msgAsString.split("(?<=" + CR + ")");
        for (String record : recordLines) {
            AstmRecord astmRecord = AstmRecord.fromString(record);
            records.add(astmRecord);
        }

        Class<? extends AstmMsg> msgType = getMsgType(records);
        if (msgType.equals(AstmOrderMsg.class))
            return new AstmOrderMsg(records);
        else if (msgType.equals(AstmResultMsg.class))
            return new AstmResultMsg(records);
        else if (msgType.equals(AstmQueryMsg.class))
            return new AstmQueryMsg(records);
        else
            throw new RuntimeException("Unexpected message type");
    }

    public Class<? extends AstmMsg> getMsgType(List<AstmRecord> records) {
        Class<? extends AstmMsg> msgType = null;
        for (AstmRecord record : records)
            if (record instanceof O)
                msgType = AstmOrderMsg.class;
            else if (record instanceof Q)
                msgType = AstmQueryMsg.class;
            else if (record instanceof R)
                msgType = AstmResultMsg.class;
        return msgType;
    }
}