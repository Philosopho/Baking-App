package com.krinotech.bakingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeAllIngredients;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> loadRecipes();

    @Transaction
    @Query("SELECT * FROM recipe")
    LiveData<List<RecipeAllIngredients>> getRecipeWithIngredients();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipes);
}
