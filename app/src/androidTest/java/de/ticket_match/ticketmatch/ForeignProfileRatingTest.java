package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;
import android.widget.RatingBar;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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

public class ForeignProfileRatingTest {

    String Nachricht;

    @Rule

    public ActivityTestRule<ForeignProfileRating> myProfileTest = new ActivityTestRule<>(ForeignProfileRating.class);


    @Before
    public void initValidString() {

        Nachricht = "Naja.";
    }

    @Test


    public void ratingtest() throws InterruptedException{



        onView(withId(R.id.newrating_text)).perform(clearText()).perform(typeText(Nachricht)).perform(closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.newrating_text)).check(matches(withText(Nachricht)));

      //  onView(withClassName(Matchers.equalTo(RatingBar.class.getName()))).perform(click());
      //  onView(withId(R.id.newrating_stars)).perform(click());
        onView(withId(R.id.fab_rate)).check(matches(isClickable()));
    }

}
