package ee.lis.flow_component.astm_to_string;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.interfaces.astm.msg.AstmMsg;
import ee.lis.util.CommonProtocol.RecipientConf;
import ee.lis.core.FlowComponent;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class AstmToStringConverter extends FlowComponent<RecipientConf> {

    @Override
    public PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(AstmMsg.class, this::convertAndForward)
            .build();
    }

    private void convertAndForward(AstmMsg message) {
        conf.recipient.tell(message.asString(), self());
    }
}
