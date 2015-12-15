package nz.co.chrisdrake.events.data.api.model;

import java.util.Collections;

import static nz.co.chrisdrake.events.data.api.model.MockEventSessions.KEYNOTE_SESSION;
import static nz.co.chrisdrake.events.data.api.model.MockEventSessions.NEW_IN_ANDROID_SESSION;

public final class MockEvents {
    private static final Point COORDINATES = new Point(37.783, -122.401);

    static final Event KEYNOTE =
        new Event(1, "Keynote", "https://events.google.com/io2015/schedule?sid=__keynote__",
            "Join us to learn about ...", //
            KEYNOTE_SESSION.dateStart, KEYNOTE_SESSION.dateEnd, //
            COORDINATES, "Keynote Room (L3)", "Moscone West Convention Center", //
            false, true, null, KEYNOTE_SESSION.dateTimeSummary, MockEventImages.IO,
            new SessionResource(Collections.singletonList(KEYNOTE_SESSION)));

    static final Event NEW_IN_ANDROID = new Event(2, "What's new in Android",
        "https://events.google.com/io2015/schedule?sid=ea96312e-e3d3-e411-b87f-00155d5066d7",
        "This session will ...", //
        NEW_IN_ANDROID_SESSION.dateStart, NEW_IN_ANDROID_SESSION.dateEnd, //
        COORDINATES, "Room 3 (L2)", "Moscone West Convention Center", //
        false, false, null, NEW_IN_ANDROID_SESSION.dateTimeSummary, MockEventImages.IO,
        new SessionResource(Collections.singletonList(NEW_IN_ANDROID_SESSION)));

    private MockEvents() {
        throw new AssertionError("Non-instantiable.");
    }
}
