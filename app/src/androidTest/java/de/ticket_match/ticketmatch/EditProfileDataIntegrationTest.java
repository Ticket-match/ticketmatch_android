package de.ticket_match.ticketmatch;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
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
 * Created by D060644 on 6/24/2016.
 * INFO: Please be logged out before testing.
 */
public class EditProfileDataIntegrationTest {

    //Variables
    String mTestLoginMail;
    String mTestLoginPassword;
    String mTestFirstName;
    String mTestLastName;
    String mTestLocation;
    int year;
    int month;
    int day;
    String gender;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);


    @Before
    public void initValidString() {
        // Specify a valid string.
        mTestLoginMail = "m@m.com";
        mTestLoginPassword = "maxmax";
        mTestFirstName = "Maxi";
        mTestLastName = "Musterfrauchen";
        mTestLocation = "Heidelberg";
        year = 1999;
        month = 9;
        day = 29;
        gender = "Female";

    }

    @Test
    public void test() throws InterruptedException {

//Login
//ACT
        // Insert text automatically
        onView(withId(R.id.login_mail))
                .perform(typeText(mTestLoginMail));
        onView(withId(R.id.login_password))
                .perform(typeText(mTestLoginPassword));


//CHECK
        onView(withId(R.id.login_mail))
                .check(matches(withText(mTestLoginMail)));

        onView(withId(R.id.login_password))
                .check(matches(withText(mTestLoginPassword)));

        //Click final Login Button to navigate to next view
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(500);


//Edit Profile
//ACT
        // Open Edit Profile Page
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Edit profile")).perform(click());
        Thread.sleep(500);

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

//CHECK
        onView(withId(R.id.edit_myprofile_firstname))
                .check(matches(withText(mTestFirstName)));

        onView(withId(R.id.edit_myprofile_lastname))
                .check(matches(withText(mTestLastName)));

        onView(withId(R.id.edit_myprofile_birthdate)).check(matches(withText(day + "." + month + "." + year)));

        onView(withId(R.id.edit_myprofile_gender))
                .check(matches(withSpinnerText(containsString(gender))));

        onView(withId(R.id.edit_myprofile_location))
                .check(matches(withText(mTestLocation)));


        //Click final Button to navigate to next view
        onView(withId(R.id.fab_btn_edit_myprofile)).perform(click());
        Thread.sleep(5000);

//Change Photo
//ACT
        //Upload Photo
        onView(withId(R.id.myprofile_image)).perform(click());
        onView(withText("Upload Photo")).perform(click());


//CHECK
        onView(withId(R.id.myprofile_image)).check(matches(isDisplayed()));
        Thread.sleep(500);


//Logout
//ACT
        onView(withId(R.id.overflow_button)).perform(click());
        onView(withText("Logout")).perform(click());
        Thread.sleep(500);

//CHECK
        onView(withId(R.id.login_mail)).check(matches(isDisplayed()));
        Thread.sleep(500);

    }
}