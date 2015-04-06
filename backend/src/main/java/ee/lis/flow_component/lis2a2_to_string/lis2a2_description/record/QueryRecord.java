package ee.lis.flow_component.lis2a2_to_string.lis2a2_description.record;

import ee.lis.util.template_engine.CompositeEntity;
import java.util.List;
import java.util.stream.Collectors;

public class QueryRecord extends LIS2A2Record {
    public QueryRecord(CompositeEntity entity) {
        super(entity);
    }

    public List<String> getSpecimenIds() {
        List<CompositeEntity> ids = entity.repeat("Q.3").getRepeatEntities(CompositeEntity.class);
        return ids.stream().map(id -> id.simple("SPECIMEN_ID").asString()).collect(Collectors.toList());
    }
    public List<String> getAnalysisCodes() {
        List<CompositeEntity> analyses = entity.repeat("Q.5").getRepeatEntities(CompositeEntity.class);
        return analyses.stream().map(a -> a.simple("LOCAL_CODE").asString()).collect(Collectors.toList());
    }
}