package de.ticket_match.ticketmatch;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.RatingBar;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
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
    String messagenametest = "messages";

    @Rule

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void initValidString() {

        Nachricht = "Naja.";
    }

    @Test


    public void ratingtest() throws InterruptedException{

        Thread.sleep(500);
        //Open Message Tab
        onView(withContentDescription(messagenametest)).check(matches(isDisplayed()));

        onView(withContentDescription(messagenametest)).perform(click());
        Thread.sleep(800);

        //Choose Chat
        onView(withId(R.id.messages_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(500);

        onView(withId(R.id.messageInput)).check(matches(isDisplayed()));
        onView(withId(R.id.chat_with_name)).check(matches(isDisplayed()));

        onView(withId(R.id.chat_with_name)).perform(click());
        Thread.sleep(500);

        //Rate ForeignProfile

        onView(withId(R.id.foreignprofile_rating)).perform(click());
        Thread.sleep(500);

        onView(withId(R.id.newrating_text)).perform(clearText()).perform(typeText(Nachricht)).perform(closeSoftKeyboard());
        Thread.sleep(500);

        //Check
        onView(withId(R.id.newrating_text)).check(matches(withText(Nachricht)));

        //Click
        onView(withId(R.id.fab_rate)).check(matches(isClickable()));
        onView(withId(R.id.fab_rate)).perform(click());
    }

}
