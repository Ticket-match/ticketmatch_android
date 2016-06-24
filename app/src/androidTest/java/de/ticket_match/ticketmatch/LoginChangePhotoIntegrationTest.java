package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/24/2016.
 */
public class LoginChangePhotoIntegrationTest {
    //Variables

    String mTestLoginMail;
    String mTestLoginPassword;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);


    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail = "m@m.com";
        mTestLoginPassword = "maxmax";
    }

    @Test
    public void test() throws InterruptedException {

//Login
        //Insert text automatically
        onView(withId(R.id.login_mail))
                .perform(typeText(mTestLoginMail));
        onView(withId(R.id.login_password))
                .perform(typeText(mTestLoginPassword));


        //CheckValues
        onView(withId(R.id.login_mail))
                .check(matches(withText(mTestLoginMail)));

        onView(withId(R.id.login_password))
                .check(matches(withText(mTestLoginPassword)));

        //Click final Login Button to navigate to next view
        onView(withId(R.id.btn_login)).perform(click());

//Change Photo

        Thread.sleep(5000);
        //Take Photo
        onView(withId(R.id.myprofile_image)).perform(click());
        onView(withText("Take Photo")).perform(click());

        //Check
        onView(withId(R.id.myprofile_image)).check(matches(isDisplayed()));


//Logout
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Logout")).perform(click());
        Thread.sleep(500);

        //Check
        onView(withId(R.id.login_mail)).check(matches(isDisplayed()));
        Thread.sleep(500);




    }
}