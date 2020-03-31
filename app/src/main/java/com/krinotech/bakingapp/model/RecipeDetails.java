package com.krinotech.bakingapp.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeDetails {
    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "id",
            entityColumn = "recipeId",
            entity = Ingredient.class
    )
    public List<Ingredient> ingredients;

    @Relation(
            parentColumn = "id",
            entityColumn = "recipeId",
            entity = Step.class
    )
    public List<Step> steps;

    @Override
    public String toString() {
        return "Name: " + recipe.getName() + " Servings" + recipe.getServings() + " Ingredients: " + ingredients.toString() + " Steps: " + steps.toString();
    }
}

