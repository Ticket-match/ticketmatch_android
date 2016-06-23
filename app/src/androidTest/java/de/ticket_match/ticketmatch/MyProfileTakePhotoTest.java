package de.ticket_match.ticketmatch;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import com.firebase.client.core.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.ticket_match.ticketmatch.entities.Ticket;

import static android.app.PendingIntent.getActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


/**
 * Created by D060644 on 6/22/2016.
 */
public class MyProfileTakePhotoTest{

    // IntentsTestRule is an extension of ActivityTestRule. IntentsTestRule sets up Espresso-Intents
    // before each Test is executed to allow stubbing and validation of intents.
    @Rule
    public IntentsTestRule<MainActivityTabHost> intentsRule = new IntentsTestRule<>(MainActivityTabHost.class);


    @Test
    public void validateCameraScenario() throws InterruptedException {

        // Create a bitmap we can use for our simulated camera image
       Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(toPackage("com.android.camera2")).respondWith(result);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        onView(withId(R.id.myprofile_image)).perform(click());
        onView(withText("Take Photo")).perform(click());


//        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(toPackage("com.android.camera2"));

        // Take a Picture
    //    onView(withResourceName("com.android.shutter_button")).perform(click());



    }
}