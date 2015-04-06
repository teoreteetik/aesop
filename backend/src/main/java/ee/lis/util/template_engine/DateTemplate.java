package ee.lis.util.template_engine;


import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTemplate implements Template {

    private final SimpleDateFormat dateFormat;

    public DateTemplate(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public DateEntity createEntity() {
        return new DateEntity(this);
    }

    @Override
    public DateEntity parseEntityFromString(String entityAsString) {
        try {
            DateEntity entity = new DateEntity(this);
            entity.setValue(dateFormat.parse(entityAsString));
            return entity;
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date string: " + entityAsString + ". Expected format " + dateFormat.toPattern());
        }
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public String toString() {
        return "DateTemplate{" +
            "dateFormat=" + dateFormat +
            '}';
    }
}