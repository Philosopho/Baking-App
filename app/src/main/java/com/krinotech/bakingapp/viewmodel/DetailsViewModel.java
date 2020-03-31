package com.krinotech.bakingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.network.RecipeRepository;

public class DetailsViewModel extends ViewModel {

    private LiveData<RecipeDetails> recipeDetails;


    public DetailsViewModel(RecipeRepository recipeRepository, int id) {
        recipeDetails = recipeRepository.getRecipeDetails(id);
    }

    public LiveData<RecipeDetails> getRecipeDetails() {
        return recipeDetails;
    }
}
