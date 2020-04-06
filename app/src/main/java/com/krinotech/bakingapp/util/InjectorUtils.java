package com.krinotech.bakingapp.util;

import android.content.Context;

import androidx.test.espresso.IdlingResource;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
import com.krinotech.bakingapp.RecipesIdlingResource;
import com.krinotech.bakingapp.database.RecipeDao;
import com.krinotech.bakingapp.database.RecipeDatabase;
import com.krinotech.bakingapp.network.BakingApi;
import com.krinotech.bakingapp.network.RecipeRepository;
import com.krinotech.bakingapp.viewmodel.DetailsViewModelFactory;
import com.krinotech.bakingapp.viewmodel.MainViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InjectorUtils {
    public static final String BAKING_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static RecipeRepository provideRecipeRepository(Context context) {
        RecipeDao recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        BakingApi bakingApi = new Retrofit.Builder()
                    .baseUrl(BAKING_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(BakingApi.class);
        Preferences preferences = new Preferences(context);
        AppThreadExecutor appThreadExecutor = AppThreadExecutor.getInstance();

        return RecipeRepository.getInstance(recipeDao, preferences, appThreadExecutor, bakingApi);
    }

    private static RecipeRepository provideRecipeRepository(Context context, RecipesIdlingResource idlingResource) {
        RecipeDao recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        BakingApi bakingApi = new Retrofit.Builder()
                .baseUrl(BAKING_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BakingApi.class);
        Preferences preferences = new Preferences(context);
        AppThreadExecutor appThreadExecutor = AppThreadExecutor.getInstance();

        return RecipeRepository.getInstance(recipeDao, preferences, appThreadExecutor, bakingApi, idlingResource);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static DetailsViewModelFactory provideDetailsViewModelFactory(Context context, int id) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext());
        return new DetailsViewModelFactory(repository, id);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context, RecipesIdlingResource recipesIdlingResource) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext(), recipesIdlingResource);
        return new MainViewModelFactory(repository);
    }

    public static DetailsViewModelFactory provideDetailsViewModelFactory(Context context, int id, RecipesIdlingResource recipesIdlingResource) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext(), recipesIdlingResource);
        return new DetailsViewModelFactory(repository, id);
    }
}
