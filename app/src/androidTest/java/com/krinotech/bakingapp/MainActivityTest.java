package com.krinotech.bakingapp;

import android.content.Context;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
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

    @After
    public void tearDown() {
        if(idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
