package com.krinotech.bakingapp.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithIngredients {
    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "id",
            entityColumn = "recipeId"
    )
    public List<Ingredient> ingredients;
}
