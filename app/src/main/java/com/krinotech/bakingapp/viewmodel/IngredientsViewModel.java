package com.krinotech.bakingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.krinotech.bakingapp.model.RecipeAllIngredients;
import com.krinotech.bakingapp.network.RecipeRepository;

import java.util.List;

public class IngredientsViewModel extends AndroidViewModel {

    private LiveData<List<RecipeAllIngredients>> ingredients;

    public IngredientsViewModel(@NonNull Application application) {
        super(application);

        ingredients = RecipeRepository
                .getInstance(application.getApplicationContext())
                .getIngredients();
    }


    public LiveData<List<RecipeAllIngredients>> getIngredients() {
        return ingredients;
    }
}
