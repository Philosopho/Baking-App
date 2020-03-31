package com.krinotech.bakingapp.util;

import com.krinotech.bakingapp.model.Ingredient;

import java.util.List;

public class StringUtil {

    public static String buildIngredient(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(Ingredient ingredient: ingredients) {
            if(first){
                first = false;
            }
            else{
                builder.append("\n");
            }
            builder.append(ingredient.toString());
        }

        return builder.toString();
    }
}
