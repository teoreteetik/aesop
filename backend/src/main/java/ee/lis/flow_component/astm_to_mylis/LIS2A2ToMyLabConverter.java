package ee.lis.flow_component.astm_to_mylis;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.interfaces.MyLabMessages.*;
import ee.lis.interfaces.astm.msg.LIS2A2QueryMsg;
import ee.lis.interfaces.astm.msg.LIS2A2ResultMsg;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.P;
import ee.lis.util.CommonProtocol.RecipientConf;
import java.util.ArrayList;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class LIS2A2ToMyLabConverter extends FlowComponent<RecipientConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(LIS2A2ResultMsg.class, this::convertAndForwardResult)
            .match(LIS2A2QueryMsg.class, this::convertAndForwardQuery)
            .build();
    }

    private void convertAndForwardResult(LIS2A2ResultMsg LIS2A2ResultMsg) {
        for (MyLabResultMsg myLabResultMsg : astmResultMsgToMyLabResultMsgs(LIS2A2ResultMsg))
            conf.recipient.tell(myLabResultMsg, self());
    }

    public List<MyLabResultMsg> astmResultMsgToMyLabResultMsgs(LIS2A2ResultMsg LIS2A2ResultMsg) {
        List<MyLabResultMsg> result = new ArrayList<>();
        for (P patientRecord : LIS2A2ResultMsg.getPatientRecords()) {
            for (O orderRecord : LIS2A2ResultMsg.getOrderRecords(patientRecord)) {
                List<Analysis> analyses = LIS2A2ResultMsg.getResultRecords(orderRecord).stream().map(
                            resultRecord -> new Analysis(resultRecord.getAnalysisCode(),
                                                         resultRecord.getAnalysisName(),
                                                         new Result(resultRecord.getResultValue(),
                                                                    resultRecord.getUnit()))).collect(toList());
                        MyLabResultMsg myLisMyLISResultMsg = new MyLabResultMsg(new Order(
                                                                              new Patient(patientRecord.getFirstName(),
                                                                                          patientRecord.getSurname(),
                                                                                          patientRecord.getPatientId()),
                                                                              asList(new Container(orderRecord.getSpecimenId(),
                                                                                  analyses))));
                        result.add(myLisMyLISResultMsg);
            }
        }
        return result;
    }

    private void convertAndForwardQuery(LIS2A2QueryMsg LIS2A2QueryMsg) {
        for (MyLabQueryMsg myLabQueryMsg : astmQueryMsgToMyLabQueryMsgs(LIS2A2QueryMsg))
            conf.recipient.tell(myLabQueryMsg, self());
    }

    public List<MyLabQueryMsg> astmQueryMsgToMyLabQueryMsgs(LIS2A2QueryMsg LIS2A2QueryMsg) {
        return LIS2A2QueryMsg.getQueryRecords().stream()
            .map(queryRecord -> new MyLabQueryMsg(queryRecord.getSpecimenIds(), queryRecord.getAnalysisCodes()))
            .collect(toList());
    }
}