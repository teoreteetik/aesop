package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.LIS2A2Record;
import java.util.List;
import java.util.function.Function;

public enum LIS2A2MsgType {
    QUERY(LIS2A2QueryMsg::new),
    ORDER(LIS2A2OrderMsg::new),
    RESULT(LIS2A2ResultMsg::new);

    private final Function<List<LIS2A2Record>, LIS2A2Msg> messageFunction;

    private LIS2A2MsgType(Function<List<LIS2A2Record>, LIS2A2Msg> messageFunction) {
        this.messageFunction = messageFunction;
    }

    public LIS2A2Msg apply(List<LIS2A2Record> records) {
        return this.messageFunction.apply(records);
    }
}