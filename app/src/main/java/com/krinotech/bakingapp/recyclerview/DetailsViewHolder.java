package com.krinotech.bakingapp.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.databinding.RecipeIngredientsItemBinding;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.model.Step;

public class DetailsViewHolder extends RecyclerView.ViewHolder {
    private final RecipeIngredientsItemBinding recipeIngredientsItemBinding;

    public DetailsViewHolder(@NonNull RecipeIngredientsItemBinding recipeIngredientsItemBinding) {
        super(recipeIngredientsItemBinding.getRoot());

        this.recipeIngredientsItemBinding = recipeIngredientsItemBinding;
    }

    public void bind(String detail, RecipeDetails recipeDetails, DetailsAdapter.OnClickRecipeDetails onClick, int position) {
        recipeIngredientsItemBinding.tvStep.setText(detail);
        recipeIngredientsItemBinding.executePendingBindings();

        recipeIngredientsItemBinding.tvStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.clickDetails(recipeDetails, position);
            }
        });
    }

    public void bind(String detail, DetailsAdapter.OnClickRecipeDetails onClick) {
        recipeIngredientsItemBinding.tvStep.setText(detail);
        recipeIngredientsItemBinding.executePendingBindings();
    }

    public void bindIngredientClick(String detail, DetailsAdapter.OnClickRecipeDetails onClick) {
        recipeIngredientsItemBinding.tvStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.clickDetails(detail);
            }
        });
    }


}
