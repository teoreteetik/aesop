package ee.lis.util.template_engine;

public interface Template {
    Entity<?> createEntity();
    Entity<?> parseEntityFromString(String entityAsString);
}