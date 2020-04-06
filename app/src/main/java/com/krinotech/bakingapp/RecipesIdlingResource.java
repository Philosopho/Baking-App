package com.krinotech.bakingapp;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class RecipesIdlingResource implements IdlingResource {
    @Nullable private volatile ResourceCallback resourceCallback;

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public void setIdleState(boolean b) {
        isIdleNow.set(b);

        if (b && resourceCallback != null) {
            Log.d("RecuoeUdkubgResource", "setIdleState: " + b);

            resourceCallback.onTransitionToIdle();
        }
    }
}
