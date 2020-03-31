package com.krinotech.bakingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.network.RecipeRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipes;

    public MainViewModel(RecipeRepository recipeRepository) {
        recipes = recipeRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public boolean recipesExist() {
        List<Recipe> recipes = this.recipes.getValue();
        return recipes != null;
    }
}
