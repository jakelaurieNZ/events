package nz.co.chrisdrake.events.explore;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import nz.co.chrisdrake.events.DebugEventApp;
import nz.co.chrisdrake.events.data.api.model.MockEventResponse;
import nz.co.chrisdrake.events.ui.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class) @LargeTest public class ExploreFragmentTest {

    @Rule public ActivityTestRule<MainActivity> activityRule =
        new ActivityTestRule<>(MainActivity.class, true, false);

    @Before public void setUp() {
        getApplication().setMockMode(true);
        activityRule.launchActivity(null);
    }

    @After public void tearDown() {
        getApplication().setMockMode(false);
    }

    @Test public void checkPreconditions() {
        assertNotNull(activityRule.getActivity()
            .getSupportFragmentManager()
            .findFragmentById(MainActivity.FRAGMENT_CONTAINER_ID));
    }

    @Test public void eventName_IsDisplayed() {
        onView(withText(MockEventResponse.GOOGLE_IO.events.get(0).name)).check(
            matches(isDisplayed()));
    }

    private DebugEventApp getApplication() {
        return (DebugEventApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }
}