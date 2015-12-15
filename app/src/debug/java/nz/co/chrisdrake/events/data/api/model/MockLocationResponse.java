package nz.co.chrisdrake.events.data.api.model;

import java.util.Collections;

public final class MockLocationResponse {
    public static final LocationResource UNITED_STATES =
        new LocationResource(Collections.singletonList(MockLocations.UNITED_STATES));

    private MockLocationResponse() {
        throw new AssertionError("Non-instantiable.");
    }
}
