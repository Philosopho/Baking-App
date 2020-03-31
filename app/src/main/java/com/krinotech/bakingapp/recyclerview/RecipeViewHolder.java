package com.krinotech.bakingapp.recyclerview;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.model.Recipe;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;

    RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.tv_recipe_name);
    }

    public void bind(Recipe recipe, RecipeAdapter.OnItemClickListener onItemClickListener) {
        textView.setText(recipe.getName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.clickRecipe(recipe);
            }
        });
    }
}
