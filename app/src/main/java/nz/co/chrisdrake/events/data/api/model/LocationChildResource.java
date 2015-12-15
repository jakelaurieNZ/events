package nz.co.chrisdrake.events.data.api.model;

import java.util.List;

/** @see Location */
public class LocationChildResource {
    public final List<Location> children;

    public LocationChildResource(List<Location> children) {
        this.children = children;
    }
}
