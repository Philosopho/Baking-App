package com.krinotech.bakingapp.util;

import android.content.Context;

import com.krinotech.bakingapp.AppThreadExecutor;
import com.krinotech.bakingapp.Preferences;
import com.krinotech.bakingapp.database.RecipeDao;
import com.krinotech.bakingapp.database.RecipeDatabase;
import com.krinotech.bakingapp.network.RecipeRepository;
import com.krinotech.bakingapp.viewmodel.MainViewModelFactory;

public class InjectorUtils {

    public static RecipeRepository provideRecipeRepository(Context context) {
        RecipeDao recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        Preferences preferences = new Preferences(context);
        AppThreadExecutor appThreadExecutor = AppThreadExecutor.getInstance();

        return RecipeRepository.getInstance(recipeDao, preferences, appThreadExecutor);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        RecipeRepository repository = provideRecipeRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }
}
