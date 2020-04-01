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
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.util.StringUtil;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder> {
    public RecipeDetails recipeDetails;
    public OnClickRecipeDetails clickHandler;

    public interface OnClickRecipeDetails {
        void clickDetails(RecipeDetails recipeDetails, int position);

        void clickDetails(String string);
    }
    public DetailsAdapter(OnClickRecipeDetails clickHandler) {
        this.clickHandler = clickHandler;
    }

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
            holder.bind(Ingredient.INGREDIENTS, clickHandler);
            holder.bindIngredientClick(StringUtil.buildIngredient(recipeDetails.ingredients), this.clickHandler);
        }
        else {
            holder.bind(recipeDetails.steps.get(position - 1).getShortDescription(),
                    recipeDetails, clickHandler, position - 1);
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
