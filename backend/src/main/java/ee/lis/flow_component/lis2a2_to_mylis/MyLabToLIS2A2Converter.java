package ee.lis.flow_component.lis2a2_to_mylis;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.message.LIS2A2OrderMsg;
import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record.*;
import ee.lis.util.CommonProtocol.DestinationConf;
import ee.lis.mylab_interface.MyLabMessages.Analysis;
import ee.lis.mylab_interface.MyLabMessages.Container;
import ee.lis.mylab_interface.MyLabMessages.MyLabOrderMsg;
import ee.lis.core.FlowComponent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MyLabToLIS2A2Converter extends FlowComponent<DestinationConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(MyLabOrderMsg.class, this::convertAndForwardOrderMsg)
            .build();
    }

    private void convertAndForwardOrderMsg(MyLabOrderMsg myLabOrderMsg) {
        LIS2A2OrderMsg lis2A2OrderMsg = myLabOrderMsgToLIS2A2OrderMsg(myLabOrderMsg);
        conf.destination.tell(lis2A2OrderMsg, self());
    }

    public LIS2A2OrderMsg myLabOrderMsgToLIS2A2OrderMsg(MyLabOrderMsg myLabOrderMsg) {
        List<LIS2A2Record> records = new ArrayList<>();
        records.add(
            new HeaderRecord()
                .setMessageControlId("12345")
                .setSenderId("MyLIS")
                .setReceiverId("Analyzer")
                .setDateTime(new Date())
                .setVersionNumber("LIS2-A2")
                .setProcessingId("P")
                .setSenderAdress("York 15", "London", null, "89983", null));
        records.add(
            new PatientRecord()
                .setSequenceNumber(1)
                .setPatientId(myLabOrderMsg.order.patient.patientId)
                .setFirstName(myLabOrderMsg.order.patient.firstName)
                .setSurname(myLabOrderMsg.order.patient.surname));
        int i = 1;
        for (Container container : myLabOrderMsg.order.containers) {
            OrderRecord orderRecord = new OrderRecord()
                .setSequenceNumber(i++)
                .setSpecimenId(container.specimenId)
                .addPriorityCode("P");
            for (Analysis analysis : container.analyses)
                orderRecord.addAnalysis(null, analysis.name, null, analysis.code);
            records.add(orderRecord);
        }
        records.add(new TerminatorRecord().setSequenceNumber(1));
        return new LIS2A2OrderMsg(records);
    }
}
