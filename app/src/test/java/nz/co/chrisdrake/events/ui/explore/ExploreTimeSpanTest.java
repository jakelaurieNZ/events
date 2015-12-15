package nz.co.chrisdrake.events.ui.explore;

import android.test.suitebuilder.annotation.SmallTest;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.joda.time.LocalTime.MIDNIGHT;
import static org.junit.Assert.assertEquals;

@SmallTest public class ExploreTimeSpanTest {

    private final LocalDate january1 = new LocalDate(0);

    @Test public void interval_TimeSpans_BeginsAndEndsMidnight() {
        ExploreTimeSpan[] timeSpans = new ExploreTimeSpan[] {
            new ExploreTimeSpan(null, ExploreTimeSpan.DAY),
            new ExploreTimeSpan(null, ExploreTimeSpan.WEEK),
            new ExploreTimeSpan(null, ExploreTimeSpan.WEEKEND)
        };

        for (ExploreTimeSpan timeSpan : timeSpans) {
            Interval interval = timeSpan.interval;
            assertEquals(MIDNIGHT, interval.getStart().toLocalTime());
            assertEquals(MIDNIGHT, interval.getEnd().toLocalTime());
        }
    }

    @Test public void interval_Day_IsOneDay() {
        Interval interval = new ExploreTimeSpan(null, ExploreTimeSpan.DAY, january1).interval;
        assertEquals(january1, interval.getStart().toLocalDate());
        assertEquals(january1.plusDays(1), interval.getEnd().toLocalDate());
    }

    @Test public void interval_Week_IsSevenDays() {
        Interval interval = new ExploreTimeSpan(null, ExploreTimeSpan.WEEK, january1).interval;
        assertEquals(january1.plusDays(7), interval.getEnd().toLocalDate());
    }

    @Test public void interval_Weekend_EndsMonday() {
        Interval interval = new ExploreTimeSpan(null, ExploreTimeSpan.WEEKEND, january1).interval;
        assertEquals(DateTimeConstants.MONDAY, interval.getEnd().getDayOfWeek());
    }

    @Test public void interval_Weekend_BeginsFridayOrLater() {
        assertEquals(DateTimeConstants.FRIDAY, weekendIntervalStartDay(DateTimeConstants.THURSDAY));
        assertEquals(DateTimeConstants.FRIDAY, weekendIntervalStartDay(DateTimeConstants.FRIDAY));
        assertEquals(DateTimeConstants.SATURDAY,
            weekendIntervalStartDay(DateTimeConstants.SATURDAY));
        assertEquals(DateTimeConstants.SUNDAY, weekendIntervalStartDay(DateTimeConstants.SUNDAY));
    }

    private int weekendIntervalStartDay(int startDayOfWeek) {
        LocalDate date = january1.plusDays(startDayOfWeek - january1.getDayOfWeek());
        Interval interval = new ExploreTimeSpan(null, ExploreTimeSpan.WEEKEND, date).interval;
        return interval.getStart().getDayOfWeek();
    }
}