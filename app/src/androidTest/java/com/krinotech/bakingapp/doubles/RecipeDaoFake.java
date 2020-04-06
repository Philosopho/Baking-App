package com.krinotech.bakingapp.doubles;

import androidx.lifecycle.LiveData;

import com.krinotech.bakingapp.database.RecipeDao;
import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.model.Step;

import java.util.List;

public class RecipeDaoFake implements RecipeDao {
    List<Recipe> recipes;
    List<Ingredient> ingredients;
    List<Step> steps;


    @Override
    public LiveData<List<Recipe>> loadRecipes() {
        return new LiveDataFake<>(recipes);
    }

    @Override
    public LiveData<RecipeDetails> loadRecipeDetails(int recipeId) {
        return new LiveDataFake<>(new RecipeDetails());
    }

    @Override
    public void insertRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void insertIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public void insertSteps(List<Step> steps) {
        this.steps = steps;
    }
}
