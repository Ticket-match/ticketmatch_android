package de.ticket_match.ticketmatch;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/10/2016.
 *
 * INFO: Please be logged out before testing.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
    public void test() {
//Login
//ACT

        //Insert text automatically
        onView(withId(R.id.login_mail))
                .perform(typeText(mTestLoginMail));
        onView(withId(R.id.login_password))
                .perform(typeText(mTestLoginPassword));


//CHECK
        onView(withId(R.id.login_mail))
                .check(matches(withText(mTestLoginMail)));

        onView(withId(R.id.login_password))
                .check(matches(withText(mTestLoginPassword)));

        //Click final Button to navigate to next view
        onView(withId(R.id.btn_login)).perform(click());


    }

}

