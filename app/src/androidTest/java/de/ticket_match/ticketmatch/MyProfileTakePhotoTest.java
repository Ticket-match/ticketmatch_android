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
 * Created by D060644 on 6/22/2016.
 */
public class MyProfileTakePhotoTest{

    @Rule
    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(MainActivityTabHost.class);
    @Test
    public void test() throws InterruptedException {
        Thread.sleep(5000);
        //Take Photo
        onView(withId(R.id.myprofile_image)).perform(click());
        onView(withText("Take Photo")).perform(click());

        //Check
        onView(withId(R.id.myprofile_image)).check(matches(isDisplayed()));

       Thread.sleep(50);

    }



}
