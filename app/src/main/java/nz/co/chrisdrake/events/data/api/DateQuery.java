package nz.co.chrisdrake.events.data.api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateQuery {
    private static final DateTimeFormatter ISO_8601_DATE = DateTimeFormat.forPattern("yyyy-MM-dd");

    private final String date;

    public DateQuery(DateTime date) {
        this.date = date == null ? null : ISO_8601_DATE.print(date);
    }

    @Override public String toString() {
        return date;
    }
}