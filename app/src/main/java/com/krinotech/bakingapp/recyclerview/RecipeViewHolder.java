package com.krinotech.bakingapp.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krinotech.bakingapp.R;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    final TextView textView;

    RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.tv_recipe_name);
    }

}
