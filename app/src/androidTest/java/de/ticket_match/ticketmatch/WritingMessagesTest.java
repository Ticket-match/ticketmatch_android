package de.ticket_match.ticketmatch;

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


    @Rule

    public ActivityTestRule<Message_Chat> messageTest = new ActivityTestRule<>(Message_Chat.class);

   // public ActivityTestRule<MainActivityTabHost> mActivityRule = new ActivityTestRule<MainActivityTabHost>(MainActivityTabHost.class);
   // public IntentsTestRule<Message_Chat> myProfileIntentsTestRule = new IntentsTestRule<>(Message_Chat.class);


    @Before
    public void initValidString() {
        // Specify a valid string.

        lovelyMessage = "Hello.";
    }

    @Test


     public void writingMessage(){

         //Send Message
         onView(withId(R.id.messageInput)).perform(clearText()).perform(typeText(lovelyMessage)).perform(closeSoftKeyboard());
     //    onView(withId(R.id.fab_send_message)).perform(click());

        onView(withId(R.id.messageInput)).check(matches(withText(lovelyMessage)));

        onView(withId(R.id.fab_send_message)).check(matches(isClickable()));

    }


}
