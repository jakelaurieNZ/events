package nz.co.chrisdrake.events.data.api.model;

import java.util.List;

/** @see Event */
public class EventResource {
    public final List<Event> events;

    public EventResource(List<Event> events) {
        this.events = events;
    }
}
