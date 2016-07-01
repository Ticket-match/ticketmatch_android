package de.ticket_match.ticketmatch.IntegrationTests;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.ticket_match.ticketmatch.MainActivity;
import de.ticket_match.ticketmatch.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class SearchTicketAndSendMessageIntegrationTest {

    String mTestLoginMail;
    String mTestLoginPassword;
    String tabetest = "tickets";
    String testLocation;
    String testEvent;
    String tabmessage = "messages";
    String lovelyMessage;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        mTestLoginMail="rapunzel@turm.com";
        mTestLoginPassword="12345abc";
        testLocation = "Mannheim";
        testEvent = "Cinema";
        lovelyMessage = "Ich will das Ticket.";
    }

    @Test

    public void SearchTicketAndSendMessageTest ()throws InterruptedException {


        //Login
        onView(ViewMatchers.withId(R.id.login_mail)).perform(typeText(mTestLoginMail));
        onView(withId(R.id.login_password)).perform(typeText(mTestLoginPassword));

        onView(withId(R.id.login_mail)).check(matches(withText(mTestLoginMail)));

        onView(withId(R.id.login_password)).check(matches(withText(mTestLoginPassword)));
        onView(withId(R.id.btn_login)).perform(click());


        //Open Ticket Tab
        Thread.sleep(500);

        onView(withContentDescription(tabetest)).check(matches(isDisplayed()));

        onView(withContentDescription(tabetest)).perform(click());
        Thread.sleep(800);

        //Check

        onView(withId(R.id.fab_search_ticket)).check(matches(isDisplayed()));

        //SearchTicket
        onView(withId(R.id.fab_search_ticket)).perform(click());
        Thread.sleep(500);

        //Spinner Value (Event)
        onView(withId(R.id.event_type)).perform(click());
        Thread.sleep(500);
        onData(allOf(is(instanceOf(String.class)), is(testEvent))).perform(click());

        //Add Location
        onView(withId(R.id.eventlocation)).perform(typeText(testLocation)).perform(closeSoftKeyboard());

        //Check
        onView(withId(R.id.event_type)).check(matches(withSpinnerText(containsString(testEvent))));
        onView(withId(R.id.eventlocation)).check(matches(withText(testLocation)));

        //Submit Search - Click is possible
        onView(withId(R.id.fab_submit_ticket_search)).check(matches(isClickable()));
        onView(withId(R.id.fab_submit_ticket_search)).perform(click());
        //  onView(withClassName(Matchers.equalTo(FloatingActionButton.class.getName()))).perform(click());

        Thread.sleep(1500);
        //Check

        onView(withId(R.id.find_results)).check(matches(isDisplayed()));

        //Choose Ticket
        onView(withId(R.id.find_results)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(500);
        onView(withText("OK")).perform(click());

        Thread.sleep(500);

        //Send Message

        //Open Message Tab
        onView(withContentDescription(tabmessage)).check(matches(isDisplayed()));

        onView(withContentDescription(tabmessage)).perform(click());
        Thread.sleep(800);

        //Choose Chat
        onView(withId(R.id.messages_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);

        //Check
        onView(withId(R.id.messageInput)).check(matches(isDisplayed()));

        //Send Message
        onView(withId(R.id.messageInput)).perform(clearText()).perform(typeText(lovelyMessage)).perform(closeSoftKeyboard());
        Thread.sleep(1000);

        //Check
        onView(withId(R.id.messageInput)).check(matches(withText(lovelyMessage)));

        //Input
        onView(withId(R.id.fab_send_message)).check(matches(isClickable()));
        onView(withId(R.id.fab_send_message)).perform(click());

        Thread.sleep(500);

    }
}
