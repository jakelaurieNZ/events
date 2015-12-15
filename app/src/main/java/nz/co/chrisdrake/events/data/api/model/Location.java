package nz.co.chrisdrake.events.data.api.model;

/** @see LocationResource */
public class Location {
    public static final Location NEW_ZEALAND =
        new Location(574, "New Zealand", "New Zealand", null);

    public final int id;
    public final String name;
    public final String summary;
    public final LocationChildResource children;

    public Location(int id, String name, String summary, LocationChildResource children) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.children = children;
    }
}
