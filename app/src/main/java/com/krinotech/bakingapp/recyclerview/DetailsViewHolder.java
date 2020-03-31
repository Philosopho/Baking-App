package com.krinotech.bakingapp.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.databinding.RecipeIngredientsItemBinding;

public class DetailsViewHolder extends RecyclerView.ViewHolder {
    private final RecipeIngredientsItemBinding recipeIngredientsItemBinding;

    public DetailsViewHolder(@NonNull RecipeIngredientsItemBinding recipeIngredientsItemBinding) {
        super(recipeIngredientsItemBinding.getRoot());

        this.recipeIngredientsItemBinding = recipeIngredientsItemBinding;
    }

    public void bind(String detail) {
        recipeIngredientsItemBinding.tvStep.setText(detail);
        recipeIngredientsItemBinding.executePendingBindings();
    }

}
