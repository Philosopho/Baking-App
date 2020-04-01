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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krinotech.bakingapp.R;
import com.krinotech.bakingapp.lifecycle.LifecycleObserverComponent;
import com.krinotech.bakingapp.model.RecipeDetails;
import com.krinotech.bakingapp.model.Step;
import com.krinotech.bakingapp.recyclerview.DetailsAdapter;
import com.krinotech.bakingapp.util.InjectorUtils;
import com.krinotech.bakingapp.viewmodel.DetailsViewModel;
import com.krinotech.bakingapp.viewmodel.DetailsViewModelFactory;

import java.util.ArrayList;

public class RecipeDetailsFragment extends Fragment implements DetailsAdapter.OnClickRecipeDetails {
    public static final String TAG = RecipeDetailsFragment.class.getSimpleName();
    private DetailsAdapter detailsAdapter;
    private RecyclerView recyclerView;
    private int recipeIdSaved;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        int recipeId = getArguments().getInt(getString(R.string.RECIPE_ID_EXTRA), -1);
        recipeIdSaved = recipeId;
        if(savedInstanceState != null && recipeId == -1) {
            Log.d(TAG, "onCreateView: " + recipeId);
            recipeId = savedInstanceState.getInt(getString(R.string.RECIPE_ID_EXTRA));
        }
        recyclerView = rootView.findViewById(R.id.rv_recipe_details);

        DetailsViewModelFactory detailsViewModelFactory = InjectorUtils.provideDetailsViewModelFactory(getContext(), recipeId);

        DetailsViewModel detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory).get(DetailsViewModel.class);
        detailsViewModel.getRecipeDetails().observe(this, new Observer<RecipeDetails>() {

            @Override
            public void onChanged(RecipeDetails recipeDetails) {
                getActivity().setTitle(recipeDetails.recipe.getName());
                initRecyclerView(rootView.getContext());
                detailsAdapter.setSteps(recipeDetails);
            }
        });

        return rootView;
    }

    private void detailsFragment(String ingredients) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.DETAILS_EXTRA), ingredients);

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fl_details_container, detailsFragment)
                .commit();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new LifecycleObserverComponent(TAG).registerLifeCycle(getLifecycle());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.RECIPE_ID_EXTRA), recipeIdSaved);
    }

    private void initRecyclerView(Context context) {
        detailsAdapter = new DetailsAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(detailsAdapter);

    }

    @Override
    public void clickDetails(RecipeDetails recipeDetails, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.STEP_EXTRA), (ArrayList<Step>) recipeDetails.steps);
        bundle.putInt(getString(R.string.POSITION_EXTRA), position);

        Fragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_recipes_container, fragment, DetailsFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void clickDetails(String string) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.INGREDIENTS_EXTRA), string);

        Fragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_recipes_container, fragment, DetailsFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
