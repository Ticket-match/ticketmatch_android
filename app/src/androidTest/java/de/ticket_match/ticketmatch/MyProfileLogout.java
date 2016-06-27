package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/27/2016.
 */
public class MyProfileLogout {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test
    public void test() throws InterruptedException {

//ACT

        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Logout")).perform(click());
        Thread.sleep(500);

//CHECK
        onView(withId(R.id.login_mail)).check(matches(isDisplayed()));
        Thread.sleep(500);
    }

}
