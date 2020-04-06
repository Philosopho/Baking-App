package com.krinotech.bakingapp.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.test.espresso.IdlingResource;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
import com.krinotech.bakingapp.RecipesIdlingResource;
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
    public static final String TAG = RecipeRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipeRepository instance;

    private BakingApi bakingApi;


    private RecipeDao recipeDao;

    private Preferences preferences;

    private AppThreadExecutor executor;

    private RecipesIdlingResource idlingResource;

    private RecipeRepository(RecipeDao recipeDao, Preferences preferences,
                             AppThreadExecutor executor, BakingApi bakingApi) {
        this.recipeDao = recipeDao;
        this.preferences = preferences;
        this.executor = executor;
        this.bakingApi = bakingApi;
    }


    public static RecipeRepository getInstance(RecipeDao recipeDao, Preferences preferences,
                                               AppThreadExecutor executor, BakingApi bakingApi) {
        if(instance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance Repository");
                instance = new RecipeRepository(recipeDao, preferences, executor, bakingApi);
            }
        }
        return instance;
    }

    private RecipeRepository(RecipeDao recipeDao, Preferences preferences,
                             AppThreadExecutor executor, BakingApi bakingApi, RecipesIdlingResource idlingResource) {
        this.recipeDao = recipeDao;
        this.preferences = preferences;
        this.executor = executor;
        this.bakingApi = bakingApi;
        this.idlingResource = idlingResource;
    }


    public static RecipeRepository getInstance(RecipeDao recipeDao, Preferences preferences,
                                               AppThreadExecutor executor, BakingApi bakingApi, RecipesIdlingResource idlingResource) {
        if(instance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance Repository");
                instance = new RecipeRepository(recipeDao, preferences, executor, bakingApi, idlingResource);
            }
        }
        return instance;
    }


    public LiveData<List<Recipe>> getRecipes() {
        refreshRecipes();

        LiveData<List<Recipe>> recipes = recipeDao.loadRecipes();

        setIdlingResource(true);

        return recipes;
    }

    public LiveData<RecipeDetails> getRecipeDetails(int id) {
        refreshRecipes();

        LiveData<RecipeDetails> recipeDetails = recipeDao.loadRecipeDetails(id);

        setIdlingResource(true);

        return recipeDetails;
    }

    private void refreshRecipes() {
        setIdlingResource(false);
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

    private void setIdlingResource(boolean b) {
        if(idlingResource != null) {
            idlingResource.setIdleState(b);
        }
    }
}
