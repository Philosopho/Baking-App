package com.krinotech.bakingapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.recyclerview.RecipeAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {
    public static final String TAG = RecipesFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = rootView.findViewById(R.id.rv_recipe);

        initRecyclerView(rootView.getContext());

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void setRecipes(List<Recipe> recipes) {
        recipeAdapter.setRecipes(recipes);
    }

    private void initRecyclerView(Context context) {
        recipeAdapter = new RecipeAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recipeAdapter);

        recyclerView.setHasFixedSize(true);
    }
}
