package com.krinotech.bakingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.krinotech.bakingapp.model.RecipeWithIngredients;
import com.krinotech.bakingapp.network.RecipeRepository;

import java.util.List;

public class IngredientsViewModel extends ViewModel {

    private LiveData<List<RecipeWithIngredients>> recipeWithIngredients;

    public IngredientsViewModel(RecipeRepository recipeRepository) {
        recipeWithIngredients = recipeRepository.getRecipeWithIngredients();
    }
}
