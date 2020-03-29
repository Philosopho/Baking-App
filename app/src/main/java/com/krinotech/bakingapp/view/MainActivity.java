package com.krinotech.bakingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.model.RecipeAllIngredients;
import com.krinotech.bakingapp.view.fragment.RecipesFragment;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.network.BakingApi;
import com.krinotech.bakingapp.viewmodel.IngredientsViewModel;
import com.krinotech.bakingapp.viewmodel.MainViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        progressBar = findViewById(R.id.pb_circular);

        RecipesFragment recipesFragment = new RecipesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_recipes_container, recipesFragment)
                .commit();

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        showProgressBar();

        mainViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {

                if(recipes != null && !recipes.isEmpty()) {
                    recipesFragment.setRecipes(recipes);
                    hideProgressBar();
                }
                else {
                    showProgressBar();
                }

            }
        });

        IngredientsViewModel ingredientsViewModel = ViewModelProviders.of(this).get(IngredientsViewModel.class);

        showProgressBar();
        ingredientsViewModel.getIngredients().observe(this, new Observer<List<RecipeAllIngredients>>() {
            @Override
            public void onChanged(List<RecipeAllIngredients> recipeAllIngredients) {
                if(recipeAllIngredients != null && !recipeAllIngredients.isEmpty()) {
                    Log.d(TAG, recipeAllIngredients.toString());
                    hideProgressBar();
                }
                else {
                    showProgressBar();
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
}

