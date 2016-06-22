package de.ticket_match.ticketmatch;

import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;


import android.test.ActivityInstrumentationTestCase2;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.contrib.PickerActions;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;

import java.util.EnumSet;

/**
 * Created by D060644 on 6/21/2016.
 */
public class EditMyProfileTest {

    //Variables

    String mTestFirstName;
    String mTestLastName;
    String mTestLocation;
    int year;
    int month;
    int day;
    String gender;

    @Rule
    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(
            MainActivityTabHost.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestFirstName = "Maxi";
        mTestLastName = "Musterfrau";
        mTestLocation = "Heidelberg";
        year = 1999;
        month = 9;
        day = 29;
        gender = "Female";
    }

    @Test
    public void test()throws InterruptedException {

        // Open the overflow menu OR open the options menu,
        onView(withId(R.id.myprofile_name)).perform(click());
        Thread.sleep(5000);


        //Insert text automatically
        onView(withId(R.id.edit_myprofile_firstname)).perform(clearText())
                .perform(typeText(mTestFirstName));

        onView(withId(R.id.edit_myprofile_lastname)).perform(clearText())
                .perform(typeText(mTestLastName));



        // Datepicker Birthdate
       onView(withId(R.id.edit_myprofile_birthdate)).perform(click());
      onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
       onView(withText("OK")).perform(click());

        //Spinner Value (Gender)
        onView(withId(R.id.edit_myprofile_gender)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(gender))).perform(click());

        onView(withId(R.id.edit_myprofile_location)).perform(clearText())
                .perform(typeText(mTestLocation)).perform(closeSoftKeyboard());



        //Check Values

        onView(withId(R.id.edit_myprofile_firstname))
                .check(matches(withText(mTestFirstName)));

        onView(withId(R.id.edit_myprofile_lastname))
                .check(matches(withText(mTestLastName)));


        onView(withId(R.id.edit_myprofile_birthdate)).check(matches(withText(day + "." + month + "." + year)));

        onView(withId(R.id.edit_myprofile_gender))
                .check(matches(withSpinnerText(containsString(gender))));

        onView(withId(R.id.edit_myprofile_location))
                .check(matches(withText(mTestLocation)));


        //Click final Save Button to navigate to next view
        onView(withId(R.id.fab_btn_edit_myprofile)).perform(click());





    }
}
