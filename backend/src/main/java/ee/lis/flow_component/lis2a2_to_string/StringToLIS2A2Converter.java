package ee.lis.flow_component.lis2a2_to_string;

import static ee.lis.util.LowLevelUtils.CR;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message.LIS2A2Msg;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message.LIS2A2MsgType;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.*;
import ee.lis.util.CommonProtocol.DestinationConf;
import ee.lis.core.FlowComponent;
import ee.lis.util.template_engine.CompositeEntity;
import java.util.ArrayList;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class StringToLIS2A2Converter extends FlowComponent<DestinationConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(String.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(String msgAsString) {
        LIS2A2Msg lis2A2Msg = stringMsgToLIS2A2Msg(msgAsString);
        conf.destination.tell(lis2A2Msg, self());
    }

    public LIS2A2Msg stringMsgToLIS2A2Msg(String msgAsString) {
        List<LIS2A2Record> records = new ArrayList<>();
        String[] recordLines = msgAsString.split("(?<=" + CR + ")");
        for (String record : recordLines) {
            LIS2A2RecordType recordType = getRecordType(record);
            CompositeEntity recordEntity = (recordType.template.parseEntityFromString(record));
            records.add(recordType.recordFunction.apply(recordEntity));
        }
        LIS2A2MsgType msgType = getMsgType(records);
        return msgType.apply(records);
    }

    public LIS2A2MsgType getMsgType(List<LIS2A2Record> records) {
        LIS2A2MsgType msgType = null;
        for (LIS2A2Record record : records) {
            if (record instanceof OrderRecord) {
                msgType = LIS2A2MsgType.ORDER;
            } else if (record instanceof QueryRecord)
                msgType = LIS2A2MsgType.QUERY;
            else if (record instanceof ResultRecord)
                msgType = LIS2A2MsgType.RESULT;
        }
        return msgType;
    }

    public LIS2A2RecordType getRecordType(String record) {
        String recordType = record.substring(0, record.indexOf('|'));
        return LIS2A2RecordType.valueOf(recordType);
    }
}
