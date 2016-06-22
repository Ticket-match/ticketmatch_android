package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by D060644 on 6/21/2016.
 */
public class MyProfileAddInterestTest {


    //Variables

    String mTestInterest;
    @Rule
    public ActivityTestRule<MyProfile> mActivityRule = new ActivityTestRule<MyProfile>(
            MyProfile.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestInterest = "Autos";
            }

    @Test
    public void test() {

        //Insert text automatically
        onView(withId(R.id.newinterest_text))
              .perform(typeText(mTestInterest)).perform(closeSoftKeyboard());

       //Click final Add Button to add new interest
        onView(withId(R.id.btn_newinterest))
                .perform(click());

        //CheckValues
        onView(withId(R.id.newinterest_text))
                .check(matches(withText(mTestInterest)));
    }
}