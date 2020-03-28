package com.krinotech.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.krinotech.bakingapp.fragment.RecipesFragment;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.network.BakingApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;
    private FragmentManager fragmentManager;
    private RecipesFragment recipesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        progressBar = findViewById(R.id.pb_circular);

        recipesFragment = new RecipesFragment();

        fragmentManager = getSupportFragmentManager();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baking_api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingApi bakingApi = retrofit.create(BakingApi.class);

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_recipes_container, recipesFragment)
                .commit();


        Call<List<Recipe>> recipes = bakingApi.listRecipes();

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());

        fetchRecipes(recipes, recipesFragment);
    }

    private void fetchRecipes(Call<List<Recipe>> recipes, RecipesFragment fragment) {
        showProgressBar();
        recipes.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {
                    hideProgressBar();

                    fragment.setRecipes(response.body());

                }
                else  {
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                hideProgressBar();
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

