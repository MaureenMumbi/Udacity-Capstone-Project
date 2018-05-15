package com.easyoffer.android.easyofferapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.easyoffer.android.easyofferapp.ui.MainActivity;
import com.easyoffer.android.easyofferapp.ui.OfferDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Mauryn on 5/9/2018.
 */
@RunWith(AndroidJUnit4.class)
public class ShowOffersBasicTest {

    @Rule
    public IntentsTestRule<MainActivity> intentTestRule = new IntentsTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.


        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = intentTestRule.getActivity().getIdlingResource();
//        Espresso.registerIdlingResources(mIdlingResource);
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void viewTests() {

        // onView(withId(R.id.offerlistrv)).perform(RecyclerViewActions.scrollToPosition(0));
        // onView(allOf(withId(R.id.offerlistrv), withEffectiveVisibility(VISIBLE))).perform(RecyclerViewActions.scrollToPosition(0));
        //onView(allOf(withId(R.id.offerlistrv), hasFocus())).perform(RecyclerViewActions.scrollToPosition(1));

        onView(allOf(withId(R.id.offerlistrv), isDisplayed())).perform(RecyclerViewActions.scrollToPosition(0));
        // onView(allOf(withId(R.id.offerlistrv), hasFocus())).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(allOf(withId(R.id.outletname), isDisplayed()));//.check(matches(isDisplayed()));
    }


    @Test
    public void ShowOfferDetailsWhenOfferIsClicked() {

        //https://developer.android.com/training/testing/espresso/lists.html

        // onView(allOf(withId(R.id.offerlistrv), hasFocus())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // onView(withId(R.id.offerlistrv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.offerlistrv), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(OfferDetailActivity.class.getName()));

    }

    @Test
    public void clickOfferItem_OpensDetails() {
        onView(allOf(withId(R.id.offerlistrv), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.offer_child_detail)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.details_layout), isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);

        }
    }


}
