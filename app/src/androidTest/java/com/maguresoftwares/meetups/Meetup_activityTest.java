package com.maguresoftwares.meetups;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Meetup_activityTest {

    @Rule
    public IntentsTestRule<Meetup_activity> intentsTestRule = new IntentsTestRule<>(Meetup_activity.class,true,false);

    @Test
    public void checksendquestionbutton_isdisplayed(){
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.audience)).check(matches(isDisplayed())).perform(click());
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Join The Meetup")).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.code)).check(matches(isDisplayed())).perform(typeText("Rx9T"), closeSoftKeyboard());
        onView(withId(R.id.check_key)).perform(click());

        onView(withId(R.id.text_meetuproom_name)).check(matches(withText("IOS development ")));
        onView(withId(R.id.information)).perform(click());

        pressBack();

        onView(withId(R.id.text_meetuproom_name)).check(matches(withText("IOS development ")));
    }
}