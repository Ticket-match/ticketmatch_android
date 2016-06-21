package de.ticket_match.ticketmatch;

import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
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
import android.widget.Toast;

/**
 * Created by D060644 on 6/21/2016.
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

        //Insert text automatically

        onView(withId(R.id.forgotAccount_mail))
                .perform(typeText(mTestForgotMail));

        //Check
        onView(withId(R.id.forgotAccount_mail)).check(matches(withText(mTestForgotMail)));

        //Click final Submit Button to navigate to next view

        onView(withId(R.id.btn_submit)).perform(click());

        //Check Toast Message
        //stilltodo: Program a check that toast "Password reset email is being sent" openes up --> need help with toasts
        // (-->Manual/Visual check that toast opens up is possible






    }






}
