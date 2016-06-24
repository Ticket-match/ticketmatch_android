package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/21/2016.
 * INFO: Please be logged out before testing.
 */
public class ForgotAccountTest {
    //Variables

    String mTestForgotMail;


    @Rule

    public ActivityTestRule<ForgotAccount> mActivityRule = new ActivityTestRule<>(
            ForgotAccount.class);

    @Before
    public void initValidString() {
        // Specify a valid string
        mTestForgotMail = "m@m.com";
    }

    @Test
    public void changeText_sameActivity() {

//ACT
        //Insert text automatically

        onView(withId(R.id.forgotAccount_mail))
                .perform(typeText(mTestForgotMail));

//CHECK
        onView(withId(R.id.forgotAccount_mail)).check(matches(withText(mTestForgotMail)));

        //Click final Button to navigate to next view
        onView(withId(R.id.btn_submit)).perform(click());


    }


}
