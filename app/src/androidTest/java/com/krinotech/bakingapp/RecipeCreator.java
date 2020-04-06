package com.krinotech.bakingapp;

import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeCreator {
    static String[] food = {"Apples", "Oranges", "Grapes"};
    static double[] quantities = {2.0, 0.6, 1.5, 3.5, 1, 15};
    static String[] ingredientNames = {"sugar", "salt", "oil", "sugar"};
    static String[] measures = {"grams", "tbsp", "tbs"};
    static String[] stepShort = {"Cut the ", "Smash the ", "Wash the "};
    static String[] longSteps = {"Then do this, that, this that", "Do it, do it now, and then this as well"};

    static Random random = new Random();

    public static List<Recipe> create(int totalRecipes, int totalIngredients, int totalSteps) {
        List<Recipe> recipes = new ArrayList<>();

        for(int i = 0; i < totalRecipes; i++) {
            String name = generate(3, food.length, food);
            String servings = String.valueOf(random.nextInt(20));
            String image = "";

            Recipe recipe = new Recipe(i + 1, name, servings, image);

            List<Ingredient> ingredients = new ArrayList<>();
            List<Step> steps = new ArrayList<>();

            for(int j = 0; j < totalIngredients; j++) {
                double quantity = quantities[random.nextInt(quantities.length)];
                String measure = measures[random.nextInt(measures.length)];
                String ingredientName =  ingredientNames[random.nextInt(ingredientNames.length)];
                Ingredient ingredient = new Ingredient(quantity, 0, measure, ingredientName);
                ingredients.add(ingredient);
            }

            for(int k = 0; k < totalSteps; k++ ) {
                String shortDescription = stepShort[random.nextInt(stepShort.length)];
                String longDescription = shortDescription + generate(3, longSteps.length,  longSteps) + "Done!";
                Step step = new Step(k + 1, 0, shortDescription, longDescription, "", "");
                steps.add(step);
            }

            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);

            recipes.add(recipe);
        }
        return recipes;
    }

    private static String generate(int maxTimes, int length, String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        int times = random.nextInt(maxTimes);

        for(int i = 0; i < times; i++) {
            String string = array[random.nextInt(length)];
            stringBuilder.append(string);
            if(i != times - 1) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

}
