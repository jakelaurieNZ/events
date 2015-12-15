package nz.co.chrisdrake.events;

import nz.co.chrisdrake.events.data.api.model.Location;

public final class Config {
    public static final Location DEFAULT_LOCATION = Location.NEW_ZEALAND;

    private Config() {
        throw new AssertionError("Non-instantiable.");
    }
}
