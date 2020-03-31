package com.krinotech.bakingapp.view.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
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

        MainViewModelFactory mainViewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(getActivity().getApplicationContext());
        MainViewModel mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        mainViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipes) {
                hideProgressBar();
                recipeAdapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        progressBar = rootView.findViewById(R.id.pb_circular);

        recyclerView = rootView.findViewById(R.id.rv_recipe);

        initRecyclerView(getActivity());

        showProgressBar();
        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        return rootView;
    }

    private void initRecyclerView(Context context) {
        recipeAdapter = new RecipeAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(recipeAdapter);

    }

    @Override
    public void clickRecipe(Recipe recipe) {
        Fragment fragment = new RecipeDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.RECIPE_ID_EXTRA), recipe.getId());

        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_recipes_container, fragment)
                .commit();
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
