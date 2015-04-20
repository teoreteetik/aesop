package ee.lis.flow_component.lis2a2_to_mylis;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.interfaces.MyLabMessages.Analysis;
import ee.lis.interfaces.MyLabMessages.Container;
import ee.lis.interfaces.MyLabMessages.MyLabOrderMsg;
import ee.lis.interfaces.lis2a2.msg.LIS2A2OrderMsg;
import ee.lis.interfaces.lis2a2.record.H;
import ee.lis.interfaces.lis2a2.record.L;
import ee.lis.interfaces.lis2a2.record.O;
import ee.lis.interfaces.lis2a2.record.P;
import ee.lis.util.CommonProtocol.RecipientConf;
import java.util.Date;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MyLabToLIS2A2Converter extends FlowComponent<RecipientConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(MyLabOrderMsg.class, this::convertAndForwardOrderMsg)
            .build();
    }

    private void convertAndForwardOrderMsg(MyLabOrderMsg myLabOrderMsg) {
        LIS2A2OrderMsg lis2A2OrderMsg = myLabOrderMsgToLIS2A2OrderMsg(myLabOrderMsg);
        conf.recipient.tell(lis2A2OrderMsg, self());
    }

    public LIS2A2OrderMsg myLabOrderMsgToLIS2A2OrderMsg(MyLabOrderMsg myLabOrderMsg) {
        LIS2A2OrderMsg result = new LIS2A2OrderMsg();
        result = result
            .addRecord(H.create()
                .setMessageControlId("12345")
                .setSenderId("MyLIS")
                .setReceiverId("Analyzer")
                .setDateTime(new Date())
                .setVersionNumber("LIS2-A2")
                .setProcessingId("P")
                .setSenderAdress("York 15", "London", null, "89983", null))
            .addRecord(P.create(1)
                .setPatientId(myLabOrderMsg.order.patient.patientId)
                .setFirstName(myLabOrderMsg.order.patient.firstName)
                .setSurname(myLabOrderMsg.order.patient.surname));
        int i = 1;
        for (Container container : myLabOrderMsg.order.containers) {
            O orderRecord = O.create(i++)
                .setSpecimenId(container.specimenId)
                .addPriorityCode("P");
            for (Analysis analysis : container.analyses)
                orderRecord = orderRecord.addAnalysis(null, analysis.name, null, analysis.code);
            result = result.addRecord(orderRecord);
        }
        result = result.addRecord(L.create(1));
        return result;
    }
}
