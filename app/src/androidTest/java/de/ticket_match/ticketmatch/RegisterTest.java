package de.ticket_match.ticketmatch;

        import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


/**
 * Created by D060644 on 6/10/2016.
 *
 * INFO: Please be logged out before testing. Please modify mTestEmail to avoid useing an already existing email.
 */
public class RegisterTest {

    //Variables

    String mTestFirstName;
    String mTestLastName;
    String mTestEmail;
    String mTestPassword;
    String mTestLocation;
    int year;
    int month;
    int day;
    String gender;

    @Rule

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {

        // Specify a valid string.
        mTestFirstName = "Hans";
        mTestLastName = "Mueller";
        mTestEmail = "hans.hm36776@mail.com";
        mTestPassword = "hansmueller";
        mTestLocation = "Mannheim";
        year = 2015;
        month = 2;
        day = 16;
        gender = "Male";
    }

    @Test

    public void test() throws InterruptedException {

//ACT
        onView(withId(R.id.textViewRegister)).perform(click());
        Thread.sleep(500);

        //Insert texts automatically
        onView(withId(R.id.register_firstname))
                .perform(typeText(mTestFirstName));

        onView(withId(R.id.register_lastname))
                .perform(typeText(mTestLastName));

        onView(withId(R.id.register_email))
                .perform(typeText(mTestEmail));

        onView(withId(R.id.register_password))
                .perform(typeText(mTestPassword));

        onView(withId(R.id.register_location)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.register_location))
                .perform(typeText(mTestLocation)).perform(closeSoftKeyboard());


                // Datepicker Birthdate
        onView(withId(R.id.register_birthdate)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.register_birthdate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
        onView(withText("OK")).perform(click());
        Thread.sleep(50);

                //Spinner Value (Gender)
        onView(withId(R.id.register_gender)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.register_gender)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(gender))).perform(click());



//CHECK
        onView(withId(R.id.register_firstname))
                .check(matches(withText(mTestFirstName)));

        onView(withId(R.id.register_lastname))
                .check(matches(withText(mTestLastName)));

        onView(withId(R.id.register_email))
                .check(matches(withText(mTestEmail)));

        onView(withId(R.id.register_password))
                .check(matches(withText(mTestPassword)));

        onView(withId(R.id.register_birthdate)).check(matches(withText(day + ".0" + month + "." + year)));

        onView(withId(R.id.register_gender))
                .check(matches(withSpinnerText(containsString(gender))));

        onView(withId(R.id.register_location))
                .check(matches(withText(mTestLocation)));


        //Click final Button to navigate to next view
        onView(withId(R.id.btnRegister))
                .perform(click());
        Thread.sleep(500);


    }

}