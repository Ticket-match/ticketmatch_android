package de.ticket_match.ticketmatch;

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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;



@RunWith(AndroidJUnit4.class)

public class SearchTicketTest{

    //Variables

    String testLocation;
    String testEvent;
    int year;
    int month;
    int day;


 @Rule

 public ActivityTestRule<Ticket_Search> searchActivityTestRule = new ActivityTestRule<>(Ticket_Search.class);
  // public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(MainActivityTabHost.class);
  //public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        testLocation = "Mannheim";
        testEvent = "Concert";
        year = 2016;
        month = 8;
        day = 4;
    }

    @Test

    public void test() throws InterruptedException {

//Spinner Value (Event)
        onView(withId(R.id.event_type)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

        onView(withId(R.id.event_type)).check(matches(withSpinnerText(containsString(testEvent))));

        onView(withId(R.id.eventlocation)).perform(typeText(testLocation)).perform(closeSoftKeyboard());
        onView(withId(R.id.eventlocation)).check(matches(withText(testLocation)));

        // Datepicker Date

        onView(withId(R.id.date)).check(matches(isDisplayed()));
       //onView(withId(R.id.date)).check(matches(is(withClassName(Matchers.equalTo(DatePicker.class.getName())))));

       // onView(withId(R.id.date)).perform(click());
        //Thread.sleep(500);
       // onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
      //  onView(withText("OK")).perform(click());

        //Submit Search - Click is possible
        onView(withId(R.id.fab_submit_ticket_search)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.fab_submit_ticket_search)).check(matches(isClickable()));
        //  onView(withClassName(Matchers.equalTo(FloatingActionButton.class.getName()))).perform(click());

    }







    }
