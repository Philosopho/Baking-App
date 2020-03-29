package com.krinotech.bakingapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.krinotech.bakingapp.network.RecipeRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository recipeRepository;

    public MainViewModelFactory(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(recipeRepository);
    }
}
