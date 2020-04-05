package com.krinotech.bakingapp.view.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.krinotech.bakingapp.view.MainActivity;

public class BaseFragment extends Fragment {
    private MainActivity hostActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        hostActivity = (MainActivity) getActivity();
        return null;
    }

    boolean isTablet() {
        return hostActivity.isTablet();
    }

    boolean fragmentExists(String tag) {
        return hostActivity.getSupportFragmentManager()
                .findFragmentByTag(tag) != null;
    }

    void addFragment(int containerId, Fragment fragment, String fragmentTag) {
        getFragmentManager()
                .beginTransaction()
                .add(containerId, fragment, fragmentTag)
                .commit();
    }

    void replaceFragment(int containerId, Fragment fragment, String fragmentTag) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .commit();
    }

    void replaceAndAddToBackStack(int containerId, Fragment fragment, String fragmentTag) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    boolean isLandscape() {
        return getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    ActionBar getSupportActionBar() {
        return hostActivity.getSupportActionBar();
    }
}
