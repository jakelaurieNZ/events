package nz.co.chrisdrake.events.data.api.model;

import java.util.Collections;

public final class MockLocations {
    private static final Location SAN_FRANCISCO = new Location(101, "San Francisco", null, null);

    static final Location UNITED_STATES = new Location(100, "United States", null,
        new LocationChildResource(Collections.singletonList(SAN_FRANCISCO)));

    private MockLocations() {
        throw new AssertionError("Non-instantiable.");
    }
}
