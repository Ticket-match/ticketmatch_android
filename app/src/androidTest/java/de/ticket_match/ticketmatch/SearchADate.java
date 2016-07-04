package de.ticket_match.ticketmatch;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;



import android.widget.DatePicker;
import android.widget.TimePicker;

import org.junit.Before;


import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by crudic on 27.06.2016.
 */


public class SearchADate {

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
        onView(withContentDescription("tickets")).perform(click());


        //Check

        Thread.sleep(500);
        onView(withId(R.id.offer_overview)).check(matches(isDisplayed()));


//Act
        onView(withId(R.id.fab_offer)).perform(click());


        //Check

        Thread.sleep(500);
        onView(withId(R.id.eventname)).check(matches(isDisplayed()));


        //Spinner Value (Event)
        onView(withId(R.id.event_type)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

//Act
        onView(withId(R.id.eventname)).perform(typeText("Film"));


//Act
        onView(withId(R.id.eventlocation)).perform(typeText("KinoMannheim"));

//Act
        onView(withId(R.id.numberoftickets)).perform(typeText("2"));


        // Datepicker
        onView(withId(R.id.date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(click());



        // Timerpicker
        onView(withId(R.id.time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(std,min));
        onView(withText("OK")).perform(click());


//Act
        onView(withId(R.id.checkbox_price)).perform(click());

//Act
        onView(withId(R.id.fab_submit_offer)).perform(click());



//Final_Check

        onView(withId(R.id.offeroverview_list)).check(matches(isDisplayed()));







    }



}

