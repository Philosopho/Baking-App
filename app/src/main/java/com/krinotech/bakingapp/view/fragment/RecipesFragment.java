package com.krinotech.bakingapp.view.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.krinotech.bakingapp.BuildConfig;
import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.RecipesIdlingResource;
import com.krinotech.bakingapp.databinding.FragmentRecipeBinding;
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

    private FragmentRecipeBinding fragmentRecipeBinding;
    private RecipeAdapter recipeAdapter;

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

        fragmentRecipeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container, false);

        initRecyclerView(getActivity());

        showProgressBar();
        MainViewModelFactory mainViewModelFactory;

        mainViewModelFactory = getMainViewModelFactory();

        MainViewModel mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        mainViewModel.getRecipes().observe(this, recipes -> {
            hideProgressBar();
            recipeAdapter.setRecipes(recipes);
        });

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        return fragmentRecipeBinding.getRoot();
    }

    private MainViewModelFactory getMainViewModelFactory() {
        MainViewModelFactory mainViewModelFactory;
        if(BuildConfig.DEBUG) {
            RecipesIdlingResource recipesIdlingResource = new RecipesIdlingResource();
            mainViewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(getActivity().getApplicationContext(), recipesIdlingResource);
            getHostActivity().setIdlingResource(recipesIdlingResource);
        }
        else {
            mainViewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(getActivity().getApplicationContext());
        }
        return mainViewModelFactory;
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

        fragmentRecipeBinding.rvRecipe.setLayoutManager(linearLayoutManager);

        fragmentRecipeBinding.rvRecipe.setAdapter(recipeAdapter);

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
        fragmentRecipeBinding
                .includedLayout
                .pbCircular
                .setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        fragmentRecipeBinding
                .includedLayout
                .pbCircular
                .setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fragment", RecipesFragment.TAG);
    }
}
