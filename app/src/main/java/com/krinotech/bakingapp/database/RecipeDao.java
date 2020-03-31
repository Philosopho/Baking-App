package com.krinotech.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.model.Step;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> loadRecipes();

    @Transaction
    @Query("SELECT * FROM recipe where :id")
    LiveData<RecipeDetails> loadRecipeDetails(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSteps(List<Step> steps);
}
