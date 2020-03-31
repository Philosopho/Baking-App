package com.krinotech.bakingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void clickRecipe(Recipe recipe);
    }
    public RecipeAdapter(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.bind(recipe, this.clickListener);
    }

    @Override
    public int getItemCount() {
        if(recipes != null) {
            return recipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
