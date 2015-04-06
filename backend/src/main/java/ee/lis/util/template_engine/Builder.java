package ee.lis.util.template_engine;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Builder {

    public static CompositeTemplateBuilder comp(String format) {
        return new CompositeTemplateBuilder(format);
    }
    public static DateTemplateBuilder date(String dateFormat) {
        return new DateTemplateBuilder(dateFormat);
    }
    public static SimpleTemplateBuilder simple() {
        return new SimpleTemplateBuilder();
    }
    public static RepeatTemplateBuilder repeat(String repeatDelimiter, TemplateBuilder templateBuilder) {
        return new RepeatTemplateBuilder(repeatDelimiter, templateBuilder);
    }

    public static abstract class TemplateBuilder<T extends Template> {
        public abstract T build();
    }

    public static class CompositeTemplateBuilder extends TemplateBuilder<CompositeTemplate> {
        private final Map<String, TemplateBuilder> subEntitySpecBuilders;
        private final String format;
        public CompositeTemplateBuilder(String format) {
            this.subEntitySpecBuilders = new HashMap<>();
            this.format = format;
        }
        public CompositeTemplateBuilder sub(String key, TemplateBuilder entitySpecBuilder) {
            subEntitySpecBuilders.put(key, entitySpecBuilder);
            return this;
        }
        @Override
        public CompositeTemplate build() {
            Map<String, Template> subEntitySpecs = new HashMap<>();
            for (Entry<String, TemplateBuilder> entry : subEntitySpecBuilders.entrySet()) {
                subEntitySpecs.put(entry.getKey(), entry.getValue().build());
            }
            return new CompositeTemplate(format, subEntitySpecs);
        }
    }

    public static class RepeatTemplateBuilder extends TemplateBuilder<RepeatTemplate> {
        private final String repeatDelimiter;
        private final TemplateBuilder repeatTemplateBuilder;
        public RepeatTemplateBuilder(String repeatDelimiter, TemplateBuilder repeatTemplateBuilder) {
            this.repeatDelimiter = repeatDelimiter;
            this.repeatTemplateBuilder = repeatTemplateBuilder;
        }
        @Override
        public RepeatTemplate build() {
            return new RepeatTemplate(repeatDelimiter, repeatTemplateBuilder.build());
        }
    }

    public static class DateTemplateBuilder extends TemplateBuilder<DateTemplate> {
        private final SimpleDateFormat dateFormat;
        public DateTemplateBuilder(String dateFormat) {
            this.dateFormat = new SimpleDateFormat(dateFormat);
        }
        @Override
        public DateTemplate build() {
            return new DateTemplate(dateFormat);
        }
    }

    public static class SimpleTemplateBuilder extends TemplateBuilder<SimpleTemplate> {
        @Override
        public SimpleTemplate build() {
            return new SimpleTemplate();
        }
    }
}