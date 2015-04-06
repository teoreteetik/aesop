package ee.lis.util.template_engine;

import java.util.HashMap;
import java.util.Map;

public class CompositeEntity extends Entity<CompositeTemplate> {

    private Map<String, Entity> subEntities = new HashMap<>();

    public CompositeEntity(CompositeTemplate spec) {
        super(spec);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T sub(String key, Class<T> clazz) {
        return (T) subEntities.get(key);
    }

    public SimpleEntity simple(String key) {
        return sub(key, SimpleEntity.class);
    }
    public CompositeEntity comp(String key) {
        return sub(key, CompositeEntity.class);
    }
    public RepeatEntity repeat(String key) {
        return sub(key, RepeatEntity.class);
    }
    public DateEntity date(String key) {
        return sub(key, DateEntity.class);
    }

    void setSubEntity(String key, Entity<?> value) {
        this.subEntities.put(key, value);
    }

    @Override
    public String asString() {
        String result = "";
        String format = template.getFormat();
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == CompositeTemplate.ID_START) {
                String key = "";
                while (format.charAt(++i) != CompositeTemplate.ID_END)
                    key += format.charAt(i);
                result += subEntities.get(key).asString();
            } else
                result += c;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CompositeEntity{" +
            "subEntities=" + subEntities +
            '}';
    }

    //    public void setValue(String path, String value) {
//        String[] parts = path.split("#", 2);
//        Entity<?> entity = subEntities.get(parts[0]);
//        if (parts.length == 1) {
//            SimpleEntity simpleEntity = (SimpleEntity) entity;
//            subEntities.put(parts[0], new SimpleEntity(simpleEntity.template, value));
//        } else {
//            setValue(parts[1], value);
//        }
//    }


//    @Override
//    public String getPathValue(String path) {
//        if (path.length() == 0)
//            return asString();
//        String[] parts = path.split("#", 2);
//        return getSubEntity(parts[0]).getPathValue(parts.length == 1 ? "" : parts[1]);
//    }
}
