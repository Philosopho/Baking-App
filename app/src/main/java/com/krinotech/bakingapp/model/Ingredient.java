package com.krinotech.bakingapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long recipeParentId;
    private double quantity;
    private String measure;
    private String ingredient;
    private String servings;

    public Ingredient(long id, double quantity, String measure, String ingredient, long recipeParentId, String servings) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeParentId = recipeParentId;
        this.servings = servings;
    }


    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public long getId() {
        return id;
    }

    public long getRecipeParentId() {
        return recipeParentId;
    }

    public String getServings() {
        return servings;
    }
}
