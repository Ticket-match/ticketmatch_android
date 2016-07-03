package de.ticket_match.ticketmatch;

/**
 * Created by crudic on 04.07.2016.
 */



import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.ticket_match.ticketmatch.MainActivity;
import de.ticket_match.ticketmatch.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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




public class AddANewTicketForFreeAndOneWithPrice {




    //Variables
    String testEvent;
    int year;
    int month;
    int day;
    int std;
    int min;
    int year2;
    int month2;
    int day2;
    int std2;
    int min2;



    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid int for Date-Picker

        year =2016;
        month = 9;
        day = 29;
        testEvent = "Concert";
        std = 16;
        min = 30;

        year2 =2016;
        month2 = 8;
        day2 = 29;
        testEvent = "Concert";
        std2 = 16;
        min2 = 45;
    }


    @Test

    public void test() throws InterruptedException {



        //FreeTicket


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
        onView(withId(R.id.eventname)).perform(typeText("Film2"));


//Act
        onView(withId(R.id.eventlocation)).perform(typeText("KinoMannheim"));

//Act
        onView(withId(R.id.numberoftickets)).perform(typeText("3"));


        // Datepicker
        onView(withId(R.id.date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year2, month2, day2));
        onView(withText("OK")).perform(click());



        // Timerpicker
        onView(withId(R.id.time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(std2,min2));
        onView(withText("OK")).perform(click());


//Act
        onView(withId(R.id.checkbox_price)).perform(click());

//Act
        onView(withId(R.id.fab_submit_offer)).perform(click());



//Ticket with Price

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
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(std, min));
        onView(withText("OK")).perform(click());


//Act
        onView(withId(R.id.price)).perform(typeText("3")).perform(closeSoftKeyboard());


//Act
        onView(withId(R.id.fab_submit_offer)).perform(click());


//Final_Check

        onView(withId(R.id.offeroverview_list)).check(matches(isDisplayed()));


//Search Date

        //ACT
        onView(withContentDescription("tickets")).perform(click());


        //Check

        Thread.sleep(500);
        onView(withId(R.id.offer_overview)).check(matches(isDisplayed()));

//ACT
        onView(withId(R.id.fab_search_ticket)).perform(click());


        //Check

        Thread.sleep(500);
        onView(withId(R.id.eventlocation)).check(matches(isDisplayed()));


        //Spinner Value (Event)
        onView(withId(R.id.event_type)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

//Act
        onView(withId(R.id.eventlocation)).perform(typeText("KinoMannheim"));

        // Datepicker
        onView(withId(R.id.date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(click());


//Act
        onView(withId(R.id.fab_submit_ticket_search)).perform(click());



        //Final_Check

        Thread.sleep(500);
        onView(withId(R.id.find_results)).check(matches(isDisplayed()));


    }
}
