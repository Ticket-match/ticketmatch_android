package de.ticket_match.ticketmatch;

/**
 * Created by crudic on 03.07.2016.
 */


import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;





/**
 * Created by crudic on 03.07.2016.
 */

public class MakeANewDate {

    //Variables
    String testEvent;
    int year;
    int month;
    int day;
    int std;
    int min;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid int for Date-Picker

        year = 2016;
        month = 9;
        day = 29;
        testEvent = "Concert";
        std = 16;
        min = 30;
    }

        @Test

        public void test() throws InterruptedException {

//ACT
            onView(withContentDescription("makeadate")).perform(click());


            //Check

            Thread.sleep(500);
            onView(withId(R.id.makedate_overview)).check(matches(isDisplayed()));


            //ACT
            onView(withId(R.id.fab_makeadate)).perform(click());

            //Check

            Thread.sleep(500);
            onView(withId(R.id.new_makeadate_eventname)).check(matches(isDisplayed()));



            //Insert Informations

            // Datepicker
            onView(withId(R.id.new_makeadate_date)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
            onView(withText("OK")).perform(click());

            // Timerpicker
            onView(withId(R.id.new_makeadate_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(std, min));
            onView(withText("OK")).perform(click());

            //Act
            onView(withId(R.id.new_makeadate_eventname)).perform(typeText("Star Wars"));

            //Act
            onView(withId(R.id.new_makeadate_eventlocation)).perform(typeText("Mannheim"));

            //Spinner Value (Event)
            onView(withId(R.id.new_makeadate_eventtype)).perform(click());
            Thread.sleep(500);
            onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

            //Act
            onView(withId(R.id.new_makeadate_withwoman)).perform(click()).perform(ViewActions.closeSoftKeyboard());

            //Act

            onView(withId(R.id.fab_makeadate_new)).perform(click());


            //Final-Check

            Thread.sleep(500);
            onView(withId(R.id.makedate_overview)).check(matches(isDisplayed()));




        }


}


