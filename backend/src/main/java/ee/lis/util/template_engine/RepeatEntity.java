package ee.lis.util.template_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepeatEntity extends Entity<RepeatTemplate> {
    private List<Entity<?>> repeatEntities = new ArrayList<>();

    public RepeatEntity(RepeatTemplate spec) {
        super(spec);
    }

    @Override
    public String asString() {
        return repeatEntities.stream()
            .map((entity) -> entity.asString())
            .collect(Collectors.joining(template.getRepeatDelimiter()));
    }

    public void addRepeat(Entity<?> entity) {
        repeatEntities.add(entity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Template> T getRepeatTemplate(Class<T> clazz) {
        return (T) template.getRepeatTemplate();
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity<?>> List<T> getRepeatEntities(Class<T> clazz) {
        return (List<T>) repeatEntities;
    }

    @Override
    public String toString() {
        return "RepeatEntity{" +
            "repeatEntities=" + repeatEntities +
            '}';
    }
}

//    @Override
//    public String getPathValue(String path) {
//        if (path.length() == 0)
//            return asString();
//        String[] parts = path.split("#", 2);
//        if (parts.length == 1)
//            return repeatEntities.get(Integer.valueOf(parts[0])).asString();
//        else
//            return repeatEntities.get(Integer.valueOf(parts[0])).getPathValue(parts[1]);
//    }


//    public Template<?> getRepeatTemplate() {
//        return spec;
//    }
