package com.maguresoftwares.meetups;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ScrollToAction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Login_ActivityTest {

    @Rule
    public IntentsTestRule<Login_Activity> intentsTestRule = new IntentsTestRule<>(Login_Activity.class);

    @Test
    public void signin() {
        onView(withId(R.id.login_email_button)).perform(click());
        onView(withId(R.id.inputemail)).check(matches(isDisplayed())).perform(typeText("amrut1@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.inputpassword)).check(matches(isDisplayed())).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.signin)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void check() {
        signin();
        //onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.audience)).check(matches(isDisplayed())).perform(click());

        onData(anything()).inAdapterView(withId(R.id.mainactivity_listview)).atPosition(0).perform(click());
        pressBack();

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Join The Meetup")).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.code)).check(matches(isDisplayed())).perform(typeText("Rx9T"), closeSoftKeyboard());
        onView(withId(R.id.check_key)).perform(click());

    }

    @Test
    public void input_check_question() {
        check();
        onView(withId(R.id.input_question)).perform(typeText("Question 9"),closeSoftKeyboard());
        onView(withId(R.id.send_question)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(9).onChildView(withId(R.id.questions)).check(matches(withText("Question 9")));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(1).onChildView(withId(R.id.questions)).check(matches(withText("Question 1 ")));
    }
    @Test
    public void input_check_like() {
        input_check_question();
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(9).onChildView(withId(R.id.like_button)).perform(click()).check(matches(isChecked()));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(9).onChildView(withId(R.id.number_of_likes)).check(matches(withText("1")));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(9).onChildView(withId(R.id.like_button)).perform(click()).check(matches(not(isChecked())));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(9).onChildView(withId(R.id.number_of_likes)).check(matches(withText("")));
    }
    public static void sleep(long millis) throws InterruptedException{

    }
}


