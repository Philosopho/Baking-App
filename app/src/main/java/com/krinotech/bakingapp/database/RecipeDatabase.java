package com.krinotech.bakingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 11, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase instance;
    private static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "Baking Time";

    public static RecipeDatabase getInstance(Context context) {
        if(instance == null){
            synchronized (LOCK) {
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        RecipeDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

}
