package com.krinotech.bakingapp.network;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
import com.krinotech.bakingapp.database.RecipeDao;
import com.krinotech.bakingapp.database.RecipeDatabase;
import com.krinotech.bakingapp.model.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.krinotech.bakingapp.view.fragment.RecipesFragment.TAG;

public class RecipeRepository {
    public static final String BAKING_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static final Object LOCK = new Object();
    private static RecipeRepository instance;

    private final BakingApi bakingApi = new Retrofit.Builder()
            .baseUrl(BAKING_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BakingApi.class);


    private RecipeDao recipeDao;

    private Preferences preferences;

    private RecipeRepository(Context applicationContext) {
        recipeDao = RecipeDatabase.getInstance(applicationContext).recipeDao();
        preferences = new Preferences(applicationContext);
    }


    public static RecipeRepository getInstance(Context applicationContext) {
        if(instance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance Repository");
                instance = new RecipeRepository(applicationContext);
            }
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        refreshRecipes();

        return recipeDao.loadRecipes();
    }

    private void refreshRecipes() {
        AppThreadExecutor.getInstance().diskIO().execute(() -> {
            boolean recipesExist = preferences.shouldFetchNewRecipes();

            if(!recipesExist) {
                Log.d(TAG, "refreshRecipes");
                try {
                    Response<List<Recipe>> response = bakingApi.listRecipes().execute();
                    if(response.isSuccessful()) {
                        Log.d(TAG, "successful response");
                        recipeDao.insertRecipes(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
