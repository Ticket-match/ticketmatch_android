package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

/**
 * Created by crudic on 03.07.2016.
 */

import android.support.test.rule.ActivityTestRule;
        import android.widget.TabHost;

        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.action.ViewActions.click;
        import static android.support.test.espresso.action.ViewActions.typeText;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by crudic on 27.06.2016.
 */
public class TicketScreenButton {



    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);
    @Test

    public void test() throws InterruptedException {

//ACT
      onView(withContentDescription("messages")).perform(click());


//Check

        Thread.sleep(500);
        onView(withId(R.id.messages_list)).check(matches(isDisplayed()));
    }



}