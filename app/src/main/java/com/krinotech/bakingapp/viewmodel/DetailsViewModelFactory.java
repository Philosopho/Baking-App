package com.krinotech.bakingapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.krinotech.bakingapp.network.RecipeRepository;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository recipeRepository;
    private final int id;

    public DetailsViewModelFactory(RecipeRepository recipeRepository, int id) {
        this.recipeRepository = recipeRepository;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(recipeRepository, id);
    }
}
