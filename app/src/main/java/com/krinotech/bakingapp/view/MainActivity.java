package com.krinotech.bakingapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        RecipesFragment recipesFragment = new RecipesFragment();

        if(fragmentExists(RecipesFragment.TAG)) {
            Log.d(TAG, "fragment exists");
        }
        else {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fl_recipes_container, recipesFragment, RecipesFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());
    }

    @Override
    public void onBackPressed() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if ( backStackCount == 1) {
            finish();
        } else if (backStackCount > 1) {
            if(backStackCount == 2) {
                setTitle(getString(R.string.main_activity_title));
            }
            fragmentManager.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    private boolean fragmentExists(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }
}

