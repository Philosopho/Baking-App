package com.krinotech.bakingapp.view;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.databinding.ActivityMainBinding;
import com.krinotech.bakingapp.view.fragment.RecipesFragment;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding activityMainBinding;
    private FragmentManager fragmentManager;
    private boolean isTablet;
    private boolean fromIngredientsWidget;
    private IdlingResource idlingResource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        isTablet = getResources().getBoolean(R.bool.isTablet);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if(isTablet) {
            hideSecondPane();
        }

        Intent intent = getIntent();

        if(intent != null) {
            fromIngredientsWidget = intent.getBooleanExtra(getString(R.string.INGREDIENTS_WIDGET_EXTRA), false);
        }
        else {
            fromIngredientsWidget = false;
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

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
            if (stackHeight > 1 && !isFromIngredientsWidget()) { // if we have something on the stack (doesn't include the current shown fragment)
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
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
            ViewGroup.LayoutParams layoutParams = activityMainBinding.flRecipesContainer.getLayoutParams();
            layoutParams.width = MATCH_PARENT;
            activityMainBinding.flRecipesContainer.setLayoutParams(layoutParams);
            activityMainBinding.flDetailsContainer.setVisibility(View.GONE);
            activityMainBinding.vDivider.setVisibility(View.GONE);
        }
    }

    private boolean fragmentExists(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }

    public boolean isTablet() {
        return isTablet;
    }

    public FrameLayout getSecondPane() {
        return activityMainBinding.flDetailsContainer;
    }

    public FrameLayout getFirstPane() { return activityMainBinding.flRecipesContainer; }

    public View getDivider() {
        return activityMainBinding.vDivider;
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return idlingResource;
    }

    public void setIdlingResource(IdlingResource resource) {
        this.idlingResource = resource;
    }

    public boolean isFromIngredientsWidget() {
        return fromIngredientsWidget;
    }
}

