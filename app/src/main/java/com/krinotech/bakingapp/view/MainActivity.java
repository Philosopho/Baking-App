package com.krinotech.bakingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.recyclerview.RecipeAdapter;
import com.krinotech.bakingapp.util.InjectorUtils;
import com.krinotech.bakingapp.view.fragment.RecipeDetailsFragment;
import com.krinotech.bakingapp.view.fragment.RecipesFragment;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.viewmodel.MainViewModel;
import com.krinotech.bakingapp.viewmodel.MainViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        progressBar = findViewById(R.id.pb_circular);

        RecipesFragment recipesFragment = new RecipesFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_recipes_container, recipesFragment)
                .commit();

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        MainViewModelFactory mainViewModelFactory = InjectorUtils.provideMainActivityViewModelFactory(getApplicationContext());
        mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel.class);

        showProgressBar();
        mainViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                hideProgressBar();
                if(mainViewModel.recipesExist()) {
                    recipesFragment.setRecipes(recipes);
                }
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void clickRecipe(Recipe recipe) {
        Fragment fragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.RECIPE_ID_EXTRA), recipe.getId());

        fragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_recipes_container, fragment)
                .commit();

    }
}

