package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by D060644 on 6/21/2016.
 */
public class MyProfileAddInterestTest {


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

        //Insert text automatically
        onView(withId(R.id.newinterest_text))
              .perform(typeText(mTestInterest)).perform(closeSoftKeyboard());
        Thread.sleep(500);

       //Click final Add Button to add new interest
        onView(withId(R.id.btn_newinterest))
                .perform(click());
        Thread.sleep(500);

        //stilltodo: CheckValues- Listview Entry

        onView(withId(R.id.listitem_text)).check(matches(withText(mTestInterest)));



    }
}