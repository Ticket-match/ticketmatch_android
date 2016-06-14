package de.ticket_match.ticketmatch;
import android.content.Intent;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
/**
 * Created by D060644 on 6/13/2016.
 */
public class MyProfileTest {


    ActivityTestRule<MyProfile> mActivityTestRule = new ActivityTestRule<MyProfile>(
            MyProfile.class, false, false);

    public String mTestLoginMail = "hm@mail.comm";
    public String mTestLoginPassword = "hansmuller";
    public String mTestNewInterest = "Android Testing";



    @Test
    public void testMyProfile() throws Exception{

        Intent startingIntent = new Intent();
        startingIntent.putExtra(mTestLoginMail, mTestLoginPassword);

        mActivityTestRule.launchActivity(startingIntent);

        onView(withId(R.id.myprofile_name))
                .check(matches(withText("Hans Müller")));

       onView(withId(R.id.newinterest_text)).perform(typeText(mTestNewInterest));

        onView(withId(R.id.btn_newinterest)).perform(click());



    }




}
