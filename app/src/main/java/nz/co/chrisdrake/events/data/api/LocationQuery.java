package nz.co.chrisdrake.events.data.api;

import java.util.ArrayList;
import java.util.List;
import nz.co.chrisdrake.events.util.StringUtils;

public class LocationQuery {
    private final String locations;

    private LocationQuery(Builder builder) {
        this.locations =
            builder.locations == null ? null : StringUtils.join(builder.locations, ",");
    }

    @Override public String toString() {
        return locations;
    }

    public static final class Builder {
        private List<Integer> locations;

        public Builder location(int locationId) {
            if (locations == null) {
                locations = new ArrayList<>(1);
            }
            locations.add(locationId);
            return this;
        }

        public LocationQuery build() {
            return new LocationQuery(this);
        }
    }
}
