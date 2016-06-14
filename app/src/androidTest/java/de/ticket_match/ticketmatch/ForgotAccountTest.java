package de.ticket_match.ticketmatch;

import android.content.ComponentName;
import android.provider.Settings;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
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
 * Created by D060644 on 6/10/2016.
 */
public class ForgotAccountTest {

    //Variables

    String mTestForgotMail;

    @Rule

    public ActivityTestRule<ForgotAccount> mActivityRule = new ActivityTestRule<>(
            ForgotAccount.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestForgotMail = "hm@mail.comm";

    }

    @Test

    public void changeText_sameActivity() {

        //Type in Email and Password and press Login Button

        onView(withId(R.id.forgotAccount_mail))
                .perform(typeText(mTestForgotMail));


        onView(withId(R.id.btn_submit)).perform(click());

        //Check





    }

}
