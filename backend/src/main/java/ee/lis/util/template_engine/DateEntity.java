package ee.lis.util.template_engine;

import java.util.Date;

public class DateEntity extends Entity<DateTemplate> {

    private Date value;

    public DateEntity(DateTemplate template) {
        super(template);
    }

    @Override
    public String asString() {
        return value == null ? ""
                             : template.getDateFormat().format(value);
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DateEntity{" +
            "value=" + value +
            '}';
    }
}
