package ee.lis.util.template_engine;

public abstract class Entity<T extends Template> {
    final T template;

    public abstract String asString();

    Entity(T template) {
        this.template = template;
    }
}