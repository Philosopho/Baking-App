package com.krinotech.bakingapp;

import android.content.Context;
import android.content.pm.ActivityInfo;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.network.BakingApi;
import com.krinotech.bakingapp.util.InjectorUtils;
import com.krinotech.bakingapp.view.MainActivity;
import com.krinotech.bakingapp.view.fragment.DetailsFragment;
import com.krinotech.bakingapp.view.fragment.RecipeDetailsFragment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String BROWNIES = "Brownies";
    private static final String YELLOW_CAKE = "Yellow Cake";
    private static final String CHEESECAKE = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class, false, true);

    private IdlingResource idlingResource;

    @Before
    public void setUp() {
        idlingResource = testRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        if(idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


    @Test
    public void title_matches_defaultMainTitle() {
        String title = testRule
                .getActivity()
                .getResources()
                .getString(R.string.main_activity_title);

        String resultTitle = (String) testRule.getActivity().getTitle();

        assertEquals(title, resultTitle);
    }

    @Test
    public void nutellaPie_isDisplayed() {
        onView(withText(NUTELLA_PIE)).check(matches(isDisplayed()));
    }

    @Test
    public void brownies_isDisplayed() {
        onView(withText(BROWNIES)).check(matches(isDisplayed()));
    }

    @Test
    public void yellowCake_isDisplayed() {
        onView(withText(YELLOW_CAKE)).check(matches(isDisplayed()));
    }

    @Test
    public void cheeseCake_isDisplayed() {
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(CHEESECAKE)).check(matches(isDisplayed()));
    }

    @Test
    public void tap_nutellaPie_recipeName_shouldMatchTitle() {
        onView(withText(NUTELLA_PIE)).perform(click());

        String title = (String) testRule.getActivity().getTitle();

        assertEquals(title, NUTELLA_PIE);
    }

    @Test
    public void pressBack_afterTapRecipe_shouldBeDefaultTitle() {
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.pressBack();

        String resultTitle = (String) testRule.getActivity().getTitle();

        String expectedTitle = testRule.getActivity().getResources().getString(R.string.main_activity_title);

        assertEquals(expectedTitle, resultTitle);
    }

    @Test
    public void pressBack_afterTapRecipe_shouldStillSeeRecipes() {
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.pressBack();

        onView(withText(NUTELLA_PIE)).check(matches(isDisplayed()));
        onView(withText(BROWNIES)).check(matches(isDisplayed()));
        onView(withText(YELLOW_CAKE)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(CHEESECAKE)).check(matches(isDisplayed()));
    }

    @Test
    public void tapRecipe_tapStep_titleShouldMatchThatRecipe() {
        onView(withText(NUTELLA_PIE)).perform(click());
        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        String resultTitle = (String) testRule.
                getActivity().getTitle();

        assertEquals(NUTELLA_PIE, resultTitle);
    }

    @Test
    public void tapRecipe_tapFirstStep_shouldOnlyHaveNextButton() {
        onView(withText(NUTELLA_PIE)).perform(click());
        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.btn_previous_step)).check(matches(not(isDisplayed())));
        onView(withId(R.id.btn_next_step)).check(matches(isDisplayed()));
    }

    @Test
    public void tapRecipe_tapFirstStep_tapNextBtn_previousButtonIsDisplayed() {
        onView(withText(NUTELLA_PIE)).perform(click());
        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.btn_previous_step)).check(matches(not(isDisplayed())));

        onView(withId(R.id.btn_next_step)).perform(click());


        onView(withId(R.id.btn_next_step)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_previous_step)).check(matches(isDisplayed()));
    }

    @Test
    public void tapRecipe_tapLastStep_shouldOnlyHavePreviousButton() {
        onView(withText(NUTELLA_PIE)).perform(click());

        int lastPosition = ((RecipeDetailsFragment) testRule
                .getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailsFragment.TAG))
                .getDetailsAdapter()
                .getItemCount() - 1;

        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(lastPosition, click()));

        onView(withId(R.id.btn_previous_step)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next_step)).check(matches(not(isDisplayed())));
    }

    @Test
    public void tapRecipe_tapLastStep_tapPreviousButton_nextButtonIsDisplayed() {
        onView(withText(NUTELLA_PIE)).perform(click());

        int lastPosition = ((RecipeDetailsFragment) testRule
                .getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailsFragment.TAG))
                .getDetailsAdapter()
                .getItemCount() - 1;

        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(lastPosition, click()));
        onView(withId(R.id.btn_next_step)).check(matches(not(isDisplayed())));

        onView(withId(R.id.btn_previous_step)).perform(click());

        onView(withId(R.id.btn_previous_step)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_next_step)).check(matches(isDisplayed()));
    }

    @Test
    public void tapRecipe_tapThirdStep_onLandscape_shouldHideText_Buttons() {
        onView(withText(NUTELLA_PIE)).perform(click());
        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        testRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        if(testRule.getActivity().isTablet()) {
            onView(withId(R.id.btn_previous_step)).check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.btn_next_step)).check(matches(isDisplayed()));
            onView(withId(R.id.tv_step_details)).check(matches(isDisplayed()));
            onView(withId(R.id.videoView_details)).check(matches(isDisplayed()));
        }
        else {
            onView(withId(R.id.btn_previous_step)).check(matches(not(isDisplayed())));
            onView(withId(R.id.btn_next_step)).check(matches(not(isDisplayed())));
            onView(withId(R.id.tv_step_details)).check(matches(not(isDisplayed())));
            onView(withId(R.id.videoView_details)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void tapRecipe_tapThirdStep_onLandscape_toPortrait_shouldSeeText_Buttons() {
        onView(withText(NUTELLA_PIE)).perform(click());
        onView(withId(R.id.rv_recipe_details)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        testRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        testRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        onView(withId(R.id.btn_previous_step)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.btn_next_step)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_step_details)).check(matches(isDisplayed()));
        onView(withId(R.id.videoView_details)).check(matches(isDisplayed()));
    }
}
