package com.krinotech.bakingapp.application;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.krinotech.bakingapp.LifecycleEvents;

public class BakingApplication extends Application implements LifecycleObserver {
    private static final String TAG = BakingApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        Log.d(TAG, LifecycleEvents.ON_CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        Log.d(TAG, LifecycleEvents.ON_START);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        Log.d(TAG, LifecycleEvents.ON_RESUME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        Log.d(TAG, LifecycleEvents.ON_PAUSE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        Log.d(TAG, LifecycleEvents.ON_STOP);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        Log.d(TAG, LifecycleEvents.ON_DESTROY);
    }
}
