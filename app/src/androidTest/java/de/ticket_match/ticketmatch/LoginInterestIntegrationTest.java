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
 */
public class LoginInterestIntegrationTest {
    //Variables

    String mTestLoginMail;
    String mTestLoginPassword;
    String mTestInterest;
    String mTestInterest2;
    String mTestInterest3;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);



    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail = "m@m.com";
        mTestLoginPassword = "maxmax";
        mTestInterest = "Joggen";
        mTestInterest2 = "Tennis";
        mTestInterest3 = "Konzerte";
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


//Add Interest
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



        //tobedone:  CheckValues- Listview Entry

        //onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest)))


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

      //  tobedone:  CheckValues- Listview Entry

        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest3)));


        //Logout
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Logout")).perform(click());
        Thread.sleep(500);

    }




}



