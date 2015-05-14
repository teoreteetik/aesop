package ee.lis.flow_component.astm_to_mylis;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.FlowComponent;
import ee.lis.core.RecipientConf;
import ee.lis.interfaces.MyLabMessages.*;
import ee.lis.interfaces.lis2a2.msg.LIS2A2QueryMsg;
import ee.lis.interfaces.lis2a2.msg.LIS2A2ResultMsg;
import ee.lis.interfaces.lis2a2.record.O;
import ee.lis.interfaces.lis2a2.record.P;
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
        for (MyLabResultMsg myLabResultMsg : lis2a2ResultMsgToMyLabResultMsgs(LIS2A2ResultMsg))
            conf.recipient.tell(myLabResultMsg, self());
    }

    public List<MyLabResultMsg> lis2a2ResultMsgToMyLabResultMsgs(LIS2A2ResultMsg LIS2A2ResultMsg) {
        log.debug("Converting LIS2A2 message to MyLabResultMsg");
        List<MyLabResultMsg> result = new ArrayList<>();
        for (P patientRecord : LIS2A2ResultMsg.getPatientRecords()) {
            log.debug("Finding O records for " + patientRecord.asString());
            for (O orderRecord : LIS2A2ResultMsg.getOrderRecords(patientRecord)) {
                log.debug("Finding R records for " + orderRecord.asString());
                List<Analysis> analyses = LIS2A2ResultMsg.getResultRecords(orderRecord).stream().map(
                    resultRecord -> {
                        log.debug("Converting Result record to Analysis - " + resultRecord.toString());
                        return new Analysis(
                            resultRecord.getAnalysisCode(),
                            resultRecord.getAnalysisName(),
                            new Result(
                                resultRecord.getResultValue(),
                                resultRecord.getUnit()
                            )
                        );
                    }).collect(toList());

                MyLabResultMsg myLabResultMsg = new MyLabResultMsg(
                    new Order(
                        new Patient(
                            patientRecord.getFirstName(),
                            patientRecord.getSurname(),
                            patientRecord.getPatientId()
                        ),
                        asList(
                            new Container(
                                orderRecord.getSpecimenId(),
                                analyses
                            )
                        )
                    )
                );
                result.add(myLabResultMsg);
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