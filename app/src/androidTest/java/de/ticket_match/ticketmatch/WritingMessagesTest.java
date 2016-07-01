package de.ticket_match.ticketmatch;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
@RunWith(AndroidJUnit4.class)
public class WritingMessagesTest {
    String lovelyMessage;
    String messagenametest = "messages";

    @Rule

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.

        lovelyMessage = "Gib mir, kauf mir, schenk mir";
    }

    @Test

     public void writingMessage() throws InterruptedException{

        Thread.sleep(500);
        //Open Message Tab
        onView(withContentDescription(messagenametest)).check(matches(isDisplayed()));

        onView(withContentDescription(messagenametest)).perform(click());
        Thread.sleep(800);

        //Choose Chat
        onView(withId(R.id.messages_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(500);

        //Check
        onView(withId(R.id.messageInput)).check(matches(isDisplayed()));

         //Send Message
         onView(withId(R.id.messageInput)).perform(clearText()).perform(typeText(lovelyMessage)).perform(closeSoftKeyboard());
    Thread.sleep(500);


        //Check
        onView(withId(R.id.messageInput)).check(matches(withText(lovelyMessage)));

        //Input
        onView(withId(R.id.fab_send_message)).check(matches(isClickable()));
        onView(withId(R.id.fab_send_message)).perform(click());


    }


}
