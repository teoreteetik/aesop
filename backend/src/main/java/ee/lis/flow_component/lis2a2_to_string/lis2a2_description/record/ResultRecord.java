package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;
import ee.lis.util.template_engine.SimpleEntity;

public class ResultRecord extends LIS2A2Record {
    public ResultRecord(CompositeEntity entity) {
        super(entity);
    }
    public String getAnalysisCode() {
        return entity.sub("R.3", CompositeEntity.class)
                         .sub("LOCAL_CODE", SimpleEntity.class)
                             .asString();
    }
    public String getAnalysisName() {
        return entity.sub("R.3", CompositeEntity.class)
                         .sub("NAME", SimpleEntity.class)
                             .asString();
    }
    public String getResultValue() {
        return entity.sub("R.4", SimpleEntity.class).asString();
    }
    public String getUnit() {
        return entity.sub("R.5", SimpleEntity.class).asString();
    }
}