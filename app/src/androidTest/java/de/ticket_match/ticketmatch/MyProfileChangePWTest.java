package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/27/2016.
 */
public class MyProfileChangePWTest {


    //Varibles
    String mOldPW;
    String mNewPW;

    @Rule
    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(
            MainActivityTabHost.class);

    @Before
    public void initValidString() throws InterruptedException {
        // Specify a valid string.

        mOldPW = "123465";
        mNewPW = "654321";


        //Open Edit Profile Page by Clicking Edit Profile
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Change Password")).perform(click());
        Thread.sleep(5000);
//ACT

        //Insert text automatically
        onView(withId(R.id.changePassword_currentPassword)).perform(clearText())
                .perform(typeText(mOldPW));

        onView(withId(R.id.changePassword_newPassword)).perform(clearText())
                .perform(typeText(mNewPW));

        onView(withId(R.id.changePassword_newPassword_reenter)).perform(clearText())
                .perform(typeText(mNewPW)).perform(closeSoftKeyboard());

//CHECK
        onView(withId(R.id.changePassword_currentPassword)).check(matches(withText(mOldPW)));
        onView(withId(R.id.changePassword_newPassword)).check(matches(withText(mNewPW)));
        onView(withId(R.id.changePassword_newPassword_reenter)).check(matches(withText(mNewPW)));


//Click final Button to navigate to next view

        onView(withId(R.id.fab_btn_change_password)).perform(click());

        Thread.sleep(500);


    }
}