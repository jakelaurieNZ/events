package nz.co.chrisdrake.events.data.api.model;

import java.util.List;

/** @see Location */
public class LocationResource {
    public final List<Location> locations;

    public LocationResource(List<Location> locations) {
        this.locations = locations;
    }
}
