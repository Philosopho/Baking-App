package com.krinotech.bakingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.network.RecipeRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Recipe>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        recipes = RecipeRepository
                .getInstance(application.getApplicationContext())
                .getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
