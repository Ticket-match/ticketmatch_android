package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by D060644 on 6/24/2016..
 *
 * INFO: Please be logged out before testing. You have 1.5 minutes to interact with the facebook API for typing in your personal login data. After this time frame the App will shut down.
 */
public class MainActivityFacebookLoginTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test

    public void test() throws InterruptedException {

//ACT
        onView(withId(R.id.fb_login_button))
                .perform(click());

        Thread.sleep(70000);

//Check

      onView(withId(R.id.myprofile_name)).check(matches(isDisplayed()));


    }
}