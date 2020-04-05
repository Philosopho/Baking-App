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
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.model.Recipe;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.recyclerview.RecipeAdapter;
import com.krinotech.bakingapp.util.InjectorUtils;
import com.krinotech.bakingapp.view.fragment.DetailsFragment;
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
    private boolean isTablet;
    private View divider;
    private FrameLayout secondPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_main);

        if(isTablet) {
            divider = findViewById(R.id.v_divider);
            secondPane = findViewById(R.id.fl_details_container);
            hideSecondPane();
        }

        setTitle(getString(R.string.main_activity_title));

        RecipesFragment recipesFragment = new RecipesFragment();

        if(!fragmentExists(RecipesFragment.TAG)) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fl_recipes_container, recipesFragment, RecipesFragment.TAG)
                    .addToBackStack(null)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 1) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        fragmentManager.popBackStack();
        hideSecondPane();
        return true;
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
            hideSecondPane();
        }
        else {
            super.onBackPressed();
        }
    }

    private void hideSecondPane() {
        if(isTablet) {
            secondPane.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
    }

    private boolean fragmentExists(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }

    public boolean isTablet() {
        return isTablet;
    }

    public FrameLayout getSecondPane() {
        return secondPane;
    }


    public View getDivider() {
        return divider;
    }
}

