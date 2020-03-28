package com.krinotech.bakingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.krinotech.bakingapp.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
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
                        .build();
            }
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

}
