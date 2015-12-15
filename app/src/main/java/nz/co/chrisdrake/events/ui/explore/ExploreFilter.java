package nz.co.chrisdrake.events.ui.explore;

import org.joda.time.Interval;

public interface ExploreFilter {
    int getLocationId();

    int getOffset();

    Interval getInterval();

    boolean isFeaturedOnly();

    boolean isFreeOnly();
}
