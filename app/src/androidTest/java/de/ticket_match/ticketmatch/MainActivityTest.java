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
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by D060644 on 6/10/2016.
 */
public class MainActivityTest {

    //Variables

    String mTestLoginMail;
    String mTestLoginPassword;

    @Rule

 //   public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
    //        MainActivity.class);
    public IntentsTestRule< MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail = "hm@mail.comm";
        mTestLoginPassword = "hansmuller";
    }

    @Test

    public void changeText_sameActivity() {

        //Type in Email and Password and press Login Button

        onView(withId(R.id.login_mail))
                .perform(typeText(mTestLoginMail));

        onView(withId(R.id.login_password))
                .perform(typeText(mTestLoginPassword));

        onView(withId(R.id.btn_login)).perform(click());

        //Check that MyProfile Activity opens up
        intended(allOf(
               hasComponent(MyProfile.class.getName())));





    }

}
