package com.krinotech.bakingapp.util;

import com.krinotech.bakingapp.model.Ingredient;

import java.util.List;

public class StringUtil {

    public static String buildIngredient(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();

        //TODO: Figure out bug, Ingredients coming in double (duplicates)
        int size = ingredients.size();

        for(int i = 0; i < size; i++) {
            if(i == 0){
                builder.append(Ingredient.INGREDIENTS);
            }
            else {
                builder.append("\n");
            }
            builder.append(i + 1);
            builder.append(". ");
            builder.append(ingredients.get(i).toString());
        }

        return builder.toString();
    }
}
