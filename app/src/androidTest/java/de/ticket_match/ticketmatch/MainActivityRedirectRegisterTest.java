package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by D060644 on 6/21/2016.
 */
public class MainActivityRedirectRegisterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Test

    public void test(){

        onView(withId(R.id.textViewRegister)).perform(click());

        //stilltodo: Program a check that register page openes up --> need help with tabhost
        // (-->Manual/Visual check that register page opens up is possible)


    }


}
