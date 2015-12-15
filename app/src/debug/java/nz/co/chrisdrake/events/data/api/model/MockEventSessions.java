package nz.co.chrisdrake.events.data.api.model;

import java.util.Date;
import org.joda.time.LocalDate;

public final class MockEventSessions {
    private static final Date MAY_28 = LocalDate.parse("2015-05-28").toDate();

    static final Session KEYNOTE_SESSION = new Session(MAY_28, MAY_28, "May 28, 8 AM");
    static final Session NEW_IN_ANDROID_SESSION = new Session(MAY_28, MAY_28, //
        "May 28 / 1:00 PM - 2:00 PM");

    private MockEventSessions() {
        throw new AssertionError("Non-instantiable.");
    }
}
