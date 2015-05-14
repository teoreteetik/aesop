package ee.lis.flow_component.astm_to_string;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.core.RecipientConf;
import ee.lis.interfaces.lis2a2.msg.LIS2A2Msg;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class LIS2A2ToStringConverter extends FlowComponent<RecipientConf> {

    @Override
    public PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(LIS2A2Msg.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(LIS2A2Msg message) {
        conf.recipient.tell(message.asString(), self());
    }
}
