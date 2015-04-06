package ee.lis.util.template_engine;

import java.util.Collections;
import java.util.Map;

public class CompositeTemplate implements Template {
    public static final char ID_START = '{';
    public static final char ID_END = '}';

    private final String format;
    private final Map<String, Template> subEntityTemplates;

    public CompositeTemplate(String format, Map<String, Template> subEntityTemplates) {
        this.format = format;
        this.subEntityTemplates = Collections.unmodifiableMap(subEntityTemplates);
    }

    public String getFormat() {
        return format;
    }

    @Override
    public CompositeEntity createEntity() {
        CompositeEntity compositeEntity = new CompositeEntity(this);
        for (String key : subEntityTemplates.keySet())
            compositeEntity.setSubEntity(key, subEntityTemplates.get(key).createEntity());
        return compositeEntity;
    }

    @Override
    public CompositeEntity parseEntityFromString(String entityAsString) {
        // Removes redundant | and ^ from the end. //TODO make these configurable
        entityAsString = entityAsString.replaceAll("\\|$", "");
        entityAsString = entityAsString.replaceAll("\\^$", "");
        entityAsString = entityAsString.replaceAll("\r$", "");

        int formatIndex = 0;
        int stringIndex = 0;
        CompositeEntity compositeEntity = createEntity();

        while (formatIndex < format.length() && stringIndex < entityAsString.length()) {
            if (format.charAt(formatIndex) == entityAsString.charAt(stringIndex)) {
                formatIndex++;
                stringIndex++;
                continue;
            }
            // Find the identifier
            if (format.charAt(formatIndex) == ID_START) {
                String identifier = "";
                while (format.charAt(++formatIndex) != ID_END) {
                    identifier += format.charAt(formatIndex);
                }
                // Find the value from the input String that is in the place of the identifier
                String stringValue = "";
                formatIndex++;
                while (stringIndex < entityAsString.length()
                    && (format.length() == formatIndex || entityAsString.charAt(stringIndex) != format.charAt(formatIndex))) {
                    stringValue += entityAsString.charAt(stringIndex++);
                }

                // Find the Entity from the subEntities with the given code, if exists recursively evaluate its subEntities
                Template entitySpecWithFoundCode = subEntityTemplates.get(identifier);
                if (entitySpecWithFoundCode != null && stringValue.length() != 0) { // TODO
                    Entity subEntity = entitySpecWithFoundCode.parseEntityFromString(stringValue);
                    compositeEntity.setSubEntity(identifier, subEntity);
                }
            } else {
                while (stringIndex < entityAsString.length() && entityAsString.charAt(stringIndex) != format.charAt(formatIndex)) {
                    stringIndex++;
                }
            }
        }
        return compositeEntity;
    }

    @Override
    public String toString() {
        return "CompositeTemplate{" +
            "format='" + format + '\'' +
            ", subEntityTemplates=" + subEntityTemplates +
            '}';
    }
}
