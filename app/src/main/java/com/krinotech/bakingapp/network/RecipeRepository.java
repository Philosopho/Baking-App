package com.krinotech.bakingapp.network;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
import com.krinotech.bakingapp.database.RecipeDao;
import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.model.Step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecipeRepository {
    public static final String BAKING_API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String TAG = RecipeRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipeRepository instance;

    private final BakingApi bakingApi = new Retrofit.Builder()
            .baseUrl(BAKING_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BakingApi.class);


    private RecipeDao recipeDao;

    private Preferences preferences;

    private AppThreadExecutor executor;

    private RecipeRepository(RecipeDao recipeDao, Preferences preferences, AppThreadExecutor executor) {
        this.recipeDao = recipeDao;
        this.preferences = preferences;
        this.executor = executor;
    }


    public static RecipeRepository getInstance(RecipeDao recipeDao, Preferences preferences, AppThreadExecutor executor) {
        if(instance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance Repository");
                instance = new RecipeRepository(recipeDao, preferences, executor);
            }
        }
        return instance;
    }


    public LiveData<List<Recipe>> getRecipes() {
        refreshRecipes();

        return recipeDao.loadRecipes();
    }

    public LiveData<RecipeDetails> getRecipeDetails(int id) {
        refreshRecipes();

        return recipeDao.loadRecipeDetails(id);
    }

    private void refreshRecipes() {
       executor.diskIO().execute(() -> {
            boolean shouldFetchNewRecipes = preferences.shouldFetchNewRecipes();

            if(shouldFetchNewRecipes) {
                Log.d(TAG, "refreshRecipes");
                try {
                    Response<List<Recipe>> response = bakingApi.listRecipes().execute();

                    if(response.isSuccessful()) {
                        Log.d(TAG, "successful response");

                        List<Recipe> recipes = response.body();
                        List<Ingredient> ingredients = new ArrayList<>();
                        List<Step> steps = new ArrayList<>();

                        if(recipes != null) {
                            for(Recipe recipe: recipes) {
                                recipe.setRecipeIdToChildren();
                                ingredients.addAll(recipe.getIngredients());
                                steps.addAll(recipe.getSteps());
                            }
                            for(int i = 0; i < ingredients.size(); i++) {
                                ingredients.get(i).setIngredientId(i + 1);
                            }
                            for(int i = 0; i < steps.size(); i++) {
                                steps.get(i).setStepsId(i + 1);
                            }
                            recipeDao.insertRecipes(recipes);
                            recipeDao.insertIngredients(ingredients);
                            recipeDao.insertSteps(steps);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
