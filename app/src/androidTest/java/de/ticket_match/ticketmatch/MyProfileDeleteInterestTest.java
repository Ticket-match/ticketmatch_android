package de.ticket_match.ticketmatch;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by D060644 on 6/22/2016.
 */
public class MyProfileDeleteInterestTest {


    @Rule
    public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(
            MainActivityTabHost.class);

    @Before


    @Test
    public void test() throws InterruptedException {


        //Click final Delete Button to delete interest


        onView(withId(R.id.listitem_interests_delete))
                .perform(click());
        Thread.sleep(500);

        //stilltodo: CheckValues- Listview Entry



    }
}