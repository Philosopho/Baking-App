package com.krinotech.bakingapp.view.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.recyclerview.RecipeAdapter;
import com.krinotech.bakingapp.util.InjectorUtils;
import com.krinotech.bakingapp.view.MainActivity;
import com.krinotech.bakingapp.viewmodel.MainViewModel;
import com.krinotech.bakingapp.viewmodel.MainViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends BaseFragment implements RecipeAdapter.OnItemClickListener {
    public static final String TAG = RecipesFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private ProgressBar progressBar;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        progressBar = rootView.findViewById(R.id.pb_circular);

        recyclerView = rootView.findViewById(R.id.rv_recipe);

        initRecyclerView(getActivity());

        showProgressBar();
        MainViewModelFactory mainViewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(getActivity().getApplicationContext());
        MainViewModel mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        mainViewModel.getRecipes().observe(this, recipes -> {
            hideProgressBar();
            recipeAdapter.setRecipes(recipes);
        });

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        return rootView;
    }

    private void initRecyclerView(Context context) {
        recipeAdapter = new RecipeAdapter(this);

        LinearLayoutManager linearLayoutManager;
        if(isTablet()) {
            linearLayoutManager = new GridLayoutManager(
                    context, 3, RecyclerView.VERTICAL, false);
        }
        else {
            linearLayoutManager = new LinearLayoutManager(
                    context, RecyclerView.VERTICAL, false);
        }

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recipeAdapter);

    }

    @Override
    public void clickRecipe(Recipe recipe) {
        Fragment fragment = new RecipeDetailsFragment();
        Log.d(TAG, "clickRecipe: " + recipe.getId());
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.RECIPE_ID_EXTRA), recipe.getId());

        fragment.setArguments(bundle);

        replaceAndAddToBackStack(R.id.fl_recipes_container, fragment, RecipeDetailsFragment.TAG);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fragment", RecipesFragment.TAG);
    }
}
