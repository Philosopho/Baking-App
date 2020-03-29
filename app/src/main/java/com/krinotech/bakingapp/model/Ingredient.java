package com.krinotech.bakingapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private long ingredientId;

    private double quantity;

    private int recipeId;
    private String measure;
    private String ingredient;

    @Ignore
    public Ingredient(double quantity, int recipeId, String measure, String ingredient) {
        this.quantity = quantity;
        this.recipeId = recipeId;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Ingredient() {}

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
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

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
