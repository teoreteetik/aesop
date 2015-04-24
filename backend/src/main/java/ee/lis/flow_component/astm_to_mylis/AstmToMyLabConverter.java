package ee.lis.flow_component.astm_to_mylis;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.interfaces.MyLabMessages.*;
import ee.lis.interfaces.astm.msg.AstmQueryMsg;
import ee.lis.interfaces.astm.msg.AstmResultMsg;
import ee.lis.interfaces.astm.record.O;
import ee.lis.interfaces.astm.record.P;
import ee.lis.util.CommonProtocol.RecipientConf;
import java.util.ArrayList;
import java.util.List;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class AstmToMyLabConverter extends FlowComponent<RecipientConf> {
    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(AstmResultMsg.class, this::convertAndForwardResult)
            .match(AstmQueryMsg.class, this::convertAndForwardQuery)
            .build();
    }

    private void convertAndForwardResult(AstmResultMsg astmResultMsg) {
        for (MyLabResultMsg myLabResultMsg : astmResultMsgToMyLabResultMsgs(astmResultMsg))
            conf.recipient.tell(myLabResultMsg, self());
    }

    public List<MyLabResultMsg> astmResultMsgToMyLabResultMsgs(AstmResultMsg astmResultMsg) {
        List<MyLabResultMsg> result = new ArrayList<>();
        for (P patientRecord : astmResultMsg.getPatientRecords()) {
            for (O orderRecord : astmResultMsg.getOrderRecords(patientRecord)) {
                List<Analysis> analyses = astmResultMsg.getResultRecords(orderRecord).stream().map(
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

    private void convertAndForwardQuery(AstmQueryMsg astmQueryMsg) {
        for (MyLabQueryMsg myLabQueryMsg : astmQueryMsgToMyLabQueryMsgs(astmQueryMsg))
            conf.recipient.tell(myLabQueryMsg, self());
    }

    public List<MyLabQueryMsg> astmQueryMsgToMyLabQueryMsgs(AstmQueryMsg astmQueryMsg) {
        return astmQueryMsg.getQueryRecords().stream()
            .map(queryRecord -> new MyLabQueryMsg(queryRecord.getSpecimenIds(), queryRecord.getAnalysisCodes()))
            .collect(toList());
    }
}