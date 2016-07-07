package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;

/**
 * Created by D060644 on 6/23/2016.
 * INFO: Please remove interests "Running", "Tennis", "Cinema" and be logged out before testing.
 */
public class LoginInterestIntegrationTest {
    //Variables

    String mTestLoginMail;
    String mTestLoginPassword;
    String mTestInterest;
    String mTestInterest2;
    String mTestInterest3;
    String mTestInterest4;
    String mTestInterest5;
    String mTestInterest6;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);



    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail = "lisa@mail.com";
        mTestLoginPassword = "123456";
        mTestInterest = "Running";
        mTestInterest2 = "Tennis";
        mTestInterest3 = "Cinema";
        mTestInterest4 = "Fitness";
        mTestInterest5 = "Cooking";
        mTestInterest6 = "Reading";

    }

    @Test
    public void test() throws InterruptedException {

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


//Add Interest
//ACT
        //Insert text automatically
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(50);
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest2)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(50);
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest3)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest4)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest5)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.newinterest_text))
                .perform(typeText(mTestInterest6)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);

// Delete Interest
        onData(hasToString(mTestInterest))
                .inAdapterView(withId(R.id.myprofile_interests))
                .onChildView(withId(R.id.listitem_interests_delete))
                .perform(click());
        Thread.sleep(500);
        onData(hasToString(mTestInterest2))
                .inAdapterView(withId(R.id.myprofile_interests))
                .onChildView(withId(R.id.listitem_interests_delete))
                .perform(click());
        Thread.sleep(50);

//CHECK

//        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest3)));
//        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest4)));
//        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest5)));
//        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest6)));

//ACT
//Logout
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Logout")).perform(click());
        Thread.sleep(500);

    }

}



