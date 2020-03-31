package com.krinotech.bakingapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.RecipeIngredientsItemBinding;
import com.krinotech.bakingapp.model.Ingredient;
import com.krinotech.bakingapp.model.RecipeDetails;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder> {
    public RecipeDetails recipeDetails;

    public DetailsAdapter() {}

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());
        RecipeIngredientsItemBinding recipeIngredientsItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.recipe_ingredients_item, parent, false);

        return new DetailsViewHolder(recipeIngredientsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        if(position == 0) {
            holder.bind(Ingredient.INGREDIENTS);
        }
        else {
            holder.bind(recipeDetails.steps.get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if(recipeDetails != null){
            return recipeDetails.steps.size() + 1;
        }
        return 0;
    }

    public void setSteps(RecipeDetails recipeDetails) {
        this.recipeDetails = recipeDetails;
        notifyDataSetChanged();
    }
}
