package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.flow_component.lis2a2_to_string.lis2a2_description.LIS2A2RecordDescriptions;
import ee.lis.util.template_engine.CompositeEntity;
import ee.lis.util.template_engine.CompositeTemplate;
import java.util.function.Function;

public enum LIS2A2RecordType {
    H(LIS2A2RecordDescriptions.H.build(), HeaderRecord::new),
    P(LIS2A2RecordDescriptions.P.build(), PatientRecord::new),
    O(LIS2A2RecordDescriptions.O.build(), OrderRecord::new),
    Q(LIS2A2RecordDescriptions.Q.build(), QueryRecord::new),
    L(LIS2A2RecordDescriptions.L.build(), TerminatorRecord::new),
    R(LIS2A2RecordDescriptions.R.build(), ResultRecord::new),
    C(LIS2A2RecordDescriptions.C.build(), CommentRecord::new);

    public final CompositeTemplate template;
    public final Function<CompositeEntity, LIS2A2Record> recordFunction;

    private LIS2A2RecordType(CompositeTemplate template, Function<CompositeEntity, LIS2A2Record> recordFunction) {
        this.template = template;
        this.recordFunction = recordFunction;
    }



}
