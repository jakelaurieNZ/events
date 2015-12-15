package nz.co.chrisdrake.events.ui.explore;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class ExploreTimeSpan {
    @Retention(RetentionPolicy.SOURCE) @IntDef({ DAY, WEEK, WEEKEND })
    public @interface DatePeriod {
    }

    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int WEEKEND = 2;

    @Nullable public final String name;
    @NonNull public final Interval interval;

    public ExploreTimeSpan(@Nullable String name, @DatePeriod int datePeriod) {
        this(name, datePeriod, LocalDate.now());
    }

    public ExploreTimeSpan(@Nullable String name, @DatePeriod int datePeriod, LocalDate startDate) {
        this.name = name;
        this.interval = getInterval(startDate, datePeriod);
    }

    @NonNull private static Interval getInterval(LocalDate relativeStartDate,
        @DatePeriod int intervalPeriod) {
        LocalDate startDate = new LocalDate(relativeStartDate);
        LocalDate endDate = startDate.plusDays(1); // Minimum 1 day interval

        switch (intervalPeriod) {
            case DAY:
                break;
            case WEEK:
                endDate = endDate.plusDays(6);
                break;
            case WEEKEND:
                int dayOfWeek = startDate.getDayOfWeek();
                if (dayOfWeek < DateTimeConstants.FRIDAY) {
                    startDate = startDate.plusDays(DateTimeConstants.FRIDAY - dayOfWeek);
                }

                endDate = endDate.plusDays(DateTimeConstants.SUNDAY - dayOfWeek);
                break;
        }

        return new Interval(startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());
    }
}
