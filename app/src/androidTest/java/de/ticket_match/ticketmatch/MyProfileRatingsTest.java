package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by D060644 on 6/23/2016.
 */
public class MyProfileRatingsTest {

    @Rule

    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<>(
           MainActivityTabHost.class);

    @Test
    public void test() throws InterruptedException {

        //Click Ratings
       // onView(withId(R.id.myprofile_rating))
         //       .perform(click());
        Thread.sleep(5000);

        onData(withId(R.id.myprofile_rating))
                .perform(longClick());


        Thread.sleep(500);



        //Check
        onView(withId(R.id.myprofile_ratings)).check(matches(isDisplayed()));
        Thread.sleep(500);


    }}



