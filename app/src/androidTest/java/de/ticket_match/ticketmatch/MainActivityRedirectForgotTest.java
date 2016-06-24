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
 * Created by D060644 on 6/21/2016.
 *
 * INFO: Please be logged out before testing.
 */
public class MainActivityRedirectForgotTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test

    public void test() throws InterruptedException {
//ACT
        onView(withId(R.id.textView2)).perform(click());

//CHECK
        onView(withId(R.id.forgotAccount_mail)).check(matches(isDisplayed()));
        Thread.sleep(500);


    }


}
