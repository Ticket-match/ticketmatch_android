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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasToString;

/**
 * Created by D060644 on 6/22/2016.
 *
 *   INFO: Please be logged in before testing. Please make sure that interest "Blablabla" does not already exist.

 */
public class MyProfileDeleteInterestTest {

    //Variables
    String mTestInterest;

    @Rule
    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(
            MainActivityTabHost.class);

    @Before
    public void initValidString() {

        // Specify a valid string.
        mTestInterest = "Blablabla";
    }

    @Test
    public void test() throws InterruptedException {
//Add Interest
//ACT
        //Add new interest
        onView(withId(R.id.newinterest_text))
        .perform(typeText(mTestInterest)).perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);

//CHECK
        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest)));


//Delete Interest
//ACT
        onData(hasToString(mTestInterest))
                .inAdapterView(withId(R.id.myprofile_interests))
                .onChildView(withId(R.id.listitem_interests_delete))
                .perform(click());
        Thread.sleep(5000);
//CHECK

        onView(withId(R.id.listitem_text)).check(doesNotExist());


    }
}