package com.krinotech.bakingapp.util;

import android.content.Context;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
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
    public static BakingApi bakingApi;
    public static RecipeDao recipeDao;

    private static RecipeRepository provideRecipeRepository(Context context) {
        if(recipeDao == null) {
            recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        }
        if(bakingApi == null) {
            bakingApi = new Retrofit.Builder()
                    .baseUrl(BAKING_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(BakingApi.class);
        }
        Preferences preferences = new Preferences(context);
        AppThreadExecutor appThreadExecutor = AppThreadExecutor.getInstance();

        return RecipeRepository.getInstance(recipeDao, preferences, appThreadExecutor, bakingApi);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static DetailsViewModelFactory provideDetailsViewModelFactory(Context context, int id) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext());
        return new DetailsViewModelFactory(repository, id);
    }
}
