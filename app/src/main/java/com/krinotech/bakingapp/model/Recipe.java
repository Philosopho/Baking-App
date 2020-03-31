package com.krinotech.bakingapp.model;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey
    private int id;

    private String name;
    private String servings;
    private String image;

    @Ignore
    private List<Ingredient> ingredients;

    @Ignore
    private List<Step> steps;


    public Recipe(int id, String name, String servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public void setRecipeIdToChildren() {
        for(Step step: steps){
            step.setRecipeId(id);
        }

        for(Ingredient ingredient: ingredients){
            ingredient.setRecipeId(id);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
