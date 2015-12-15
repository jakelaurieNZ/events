package nz.co.chrisdrake.events.data.api;

public enum Order {
    POPULARITY("popularity"),
    DATE("date"),
    DISTANCE("distance"),
    DISTANCE_DATE("distance_date");

    private final String value;

    Order(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }
}