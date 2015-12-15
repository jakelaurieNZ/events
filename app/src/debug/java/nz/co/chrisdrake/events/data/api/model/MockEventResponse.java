package nz.co.chrisdrake.events.data.api.model;

import java.util.Arrays;
import java.util.Collections;

import static nz.co.chrisdrake.events.data.api.model.MockEvents.KEYNOTE;
import static nz.co.chrisdrake.events.data.api.model.MockEvents.NEW_IN_ANDROID;

public final class MockEventResponse {
    public static final EventResource GOOGLE_IO = new EventResource(Arrays.asList( //
        KEYNOTE, //
        NEW_IN_ANDROID));

    public static final EventResource EMPTY = new EventResource(Collections.<Event>emptyList());

    private MockEventResponse() {
        throw new AssertionError("Non-instantiable.");
    }
}
