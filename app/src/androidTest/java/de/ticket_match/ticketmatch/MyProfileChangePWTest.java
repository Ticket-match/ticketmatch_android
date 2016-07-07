package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
 *
 * INFO: Please be logged in with User - Email: lisa@mail.com and Password: 123456 before running test.
 */
public class MyProfileChangePWTest {


    //Varibles
    String mOldPW;
    String mNewPW;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Before
    public void initValidString() throws InterruptedException {
        // Specify a valid string.

        mOldPW = "123456";
        mNewPW = "654321";
    }
    @Test
     public void test() throws InterruptedException {

//Open Edit Profile Page by Clicking Edit Profile
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Change Password")).perform(click());
        Thread.sleep(500);

//Change Password
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
         Thread.sleep(2500);

//Reset Password again
//ACT
        //Open Edit Profile Page by Clicking Edit Profile
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Change Password")).perform(click());
        Thread.sleep(500);

        //Insert text automatically
        onView(withId(R.id.changePassword_currentPassword)).perform(clearText())
                .perform(typeText(mNewPW));

        onView(withId(R.id.changePassword_newPassword)).perform(clearText())
                .perform(typeText(mOldPW));

        onView(withId(R.id.changePassword_newPassword_reenter)).perform(clearText())
                .perform(typeText(mOldPW)).perform(closeSoftKeyboard());

//CHECK
        onView(withId(R.id.changePassword_currentPassword)).check(matches(withText(mNewPW)));
        onView(withId(R.id.changePassword_newPassword)).check(matches(withText(mOldPW)));
        onView(withId(R.id.changePassword_newPassword_reenter)).check(matches(withText(mOldPW)));


//Click final Button to navigate to next view

        onView(withId(R.id.fab_btn_change_password)).perform(click());

        Thread.sleep(500);



    }
}