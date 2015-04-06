package ee.lis.util.template_engine;

import java.util.regex.Pattern;

public class RepeatTemplate implements Template {
    private final String repeatDelimiter;
    private final Template repeatTemplate;

    public RepeatTemplate(String repeatDelimiter, Template repeatTemplate) {
        this.repeatDelimiter = repeatDelimiter;
        this.repeatTemplate = repeatTemplate;
    }

    @Override
    public RepeatEntity createEntity() {
        return new RepeatEntity(this);
    }

    @Override
    public RepeatEntity parseEntityFromString(String entityAsString) {
        String[] repeatParts = entityAsString.split(Pattern.quote(repeatDelimiter));
        RepeatEntity entity = new RepeatEntity(this);
        for(String repeatPart : repeatParts)
            entity.addRepeat(repeatTemplate.parseEntityFromString(repeatPart));
        return entity;
    }

    public String getRepeatDelimiter() {
        return repeatDelimiter;
    }

    public Template getRepeatTemplate() {
        return repeatTemplate;
    }

    @Override
    public String toString() {
        return "RepeatTemplate{" +
            "repeatDelimiter='" + repeatDelimiter + '\'' +
            ", repeatTemplate=" + repeatTemplate +
            '}';
    }
}