package de.ticket_match.ticketmatch;

import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;


import android.test.ActivityInstrumentationTestCase2;
import android.widget.DatePicker;
import android.widget.TimePicker;


import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.contrib.PickerActions;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
 * Created by D060644 on 6/10/2016.
 */
public class RegisterTest {

    @Rule

    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(
            Register.class);

    @Test

    public void changeText_sameActivity(){

        //Type in Email and Password and press Login

        onView(withId(R.id.register_firstname))
                .perform(typeText("Hans"));

        onView(withId(R.id.register_lastname))
                .perform(typeText("Mueller"));

        onView(withId(R.id.register_email))
                .perform(typeText("hans@mueller.com"));

        onView(withId(R.id.register_password))
                .perform(typeText("testpassword"));

        onView(withId(R.id.register_location))
                .perform(typeText("Mannheim"));

        //to do: Date picker handling
        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
       // onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1989, 8, 25));
// Click on the "OK" button to confirm and close the dialog
     // onView(withText("OK")).perform(click());

      //  onView(withId(R.id.register_birthdate_datePicker)).perform(PickerActions.setDate(1989, 8, 25));


        onView(withId(R.id.register_birthdate_datePicker)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1998, 2, 12));
        onView(withText("OK")).perform(click());


        //select spinner value

        onView(withId(R.id.register_gender)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Male"))).perform(click());
        onView(withId(R.id.register_gender))
                .check(matches(withText(containsString("Male"))));


        onView(withId(R.id.btn_register)).perform(click());





    }



}
