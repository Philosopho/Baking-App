package com.krinotech.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    public static final String RECIPE_NETWORK_RATE_LIMITER = "Recipe Rate Limiter";
    public static final String NAME = "default";

    public Preferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public boolean shouldFetchNewRecipes() {
        boolean shouldFetch = false;
        long now = System.currentTimeMillis();

        if(sharedPreferences.contains(RECIPE_NETWORK_RATE_LIMITER)) {
            long lastAcquired = sharedPreferences.getLong(RECIPE_NETWORK_RATE_LIMITER, System.currentTimeMillis());
            if(lastAcquired + (24 * 60 * 60 * 1000) < now){
                shouldFetch = true;
            }
        }
        else {
            sharedPreferences.edit().putLong(RECIPE_NETWORK_RATE_LIMITER, now).apply();
        }
        return shouldFetch;
    }
}
