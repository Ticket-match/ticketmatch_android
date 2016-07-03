package de.ticket_match.ticketmatch;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class TicketSearchResultTest {

    //Variables
    String tabetest = "tickets";
    String testLocation;
    String testEvent;
    String TOAST_STRING;

    @Rule

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        testLocation = "Mannheim";
        testEvent = "Cinema";
        TOAST_STRING = "You sent a request to buy the ticket!";
    }

    @Test

    public void test() throws InterruptedException {

        //Open Ticket Tab
        Thread.sleep(500);

        onView(withContentDescription(tabetest)).check(matches(isDisplayed()));

        onView(withContentDescription(tabetest)).perform(click());
        Thread.sleep(800);

        //Check

        onView(withId(R.id.fab_search_ticket)).check(matches(isDisplayed()));



        //SearchTicket
        onView(withId(R.id.fab_search_ticket)).perform(click());
        Thread.sleep(500);

        //Spinner Value (Event)
        onView(withId(R.id.event_type)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

        //Add Location
        onView(withId(R.id.eventlocation)).perform(typeText(testLocation)).perform(closeSoftKeyboard());


        //Check
        onView(withId(R.id.event_type)).check(matches(withSpinnerText(containsString(testEvent))));
        onView(withId(R.id.eventlocation)).check(matches(withText(testLocation)));

        //Submit Search - Click is possible
        onView(withId(R.id.fab_submit_ticket_search)).check(matches(isClickable()));
        onView(withId(R.id.fab_submit_ticket_search)).perform(click());
        //  onView(withClassName(Matchers.equalTo(FloatingActionButton.class.getName()))).perform(click());

        Thread.sleep(800);
        //Check
        onView(withId(R.id.find_results)).check(matches(isDisplayed()));

        //Choose Ticket
        onView(withId(R.id.find_results)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(500);
        onView(withText("OK")).perform(click());

    }



}
