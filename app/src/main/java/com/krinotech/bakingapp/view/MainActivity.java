package com.krinotech.bakingapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.view.fragment.RecipesFragment;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager fragmentManager;
    private boolean isTablet;
    private View divider;
    private FrameLayout secondPane;
    private FrameLayout firstPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_main);

        if(isTablet) {
            divider = findViewById(R.id.v_divider);
            secondPane = findViewById(R.id.fl_details_container);
            firstPane = findViewById(R.id.fl_recipes_container);
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

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
            if (stackHeight > 1) { // if we have something on the stack (doesn't include the current shown fragment)
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
            ViewGroup.LayoutParams layoutParams = firstPane.getLayoutParams();
            layoutParams.width = MATCH_PARENT;
            firstPane.setLayoutParams(layoutParams);
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

    public FrameLayout getFirstPane() { return firstPane; }

    public View getDivider() {
        return divider;
    }
}

