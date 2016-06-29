package de.ticket_match.ticketmatch;

import android.support.test.runner.AndroidJUnit4;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewDebug;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.design.widget.FloatingActionButton;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.startsWith;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddAndDeleteInterestsTest {

    String mTestLoginMail;
    String mTestLoginPassword;
    String mTestInterest;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail="rapunzel@turm.com";
        mTestLoginPassword="12345abc";
        mTestInterest="Schlafen";
    }

    @Test

        public void AddAndDeleteInterestsTesting ()throws InterruptedException {


        //Login
        onView(withId(R.id.login_mail)).perform(typeText(mTestLoginMail));
        onView(withId(R.id.login_password)).perform(typeText(mTestLoginPassword));

        onView(withId(R.id.login_mail)).check(matches(withText(mTestLoginMail)));

        onView(withId(R.id.login_password)).check(matches(withText(mTestLoginPassword)));

        //Click final Button to navigate to next view
        onView(withId(R.id.btn_login)).perform(click());


        //Add Interest

        //Insert text automatically
        onView(withId(R.id.newinterest_text)).perform(typeText(mTestInterest)).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_newinterest)).perform(click());
        Thread.sleep(500);

        // Delete Interest

        onData(hasToString(mTestInterest)).inAdapterView(withId(R.id.myprofile_interests)).onChildView(withId(R.id.listitem_interests_delete)).perform(click());


    }

    }

