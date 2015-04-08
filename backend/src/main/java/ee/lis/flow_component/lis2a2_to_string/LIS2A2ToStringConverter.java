package ee.lis.flow_component.lis2a2_to_string;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.interfaces.lis2a2.msg.LIS2A2Msg;
import ee.lis.util.CommonProtocol.DestinationConf;
import ee.lis.core.FlowComponent;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class LIS2A2ToStringConverter extends FlowComponent<DestinationConf> {

    @Override
    public PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(LIS2A2Msg.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(LIS2A2Msg message) {
        conf.destination.tell(message.asString(), self());
    }
}
