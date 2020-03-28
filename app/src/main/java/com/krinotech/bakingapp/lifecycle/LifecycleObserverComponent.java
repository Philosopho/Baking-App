package com.krinotech.bakingapp.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.krinotech.bakingapp.LifecycleEvents;

public class LifecycleObserverComponent implements LifecycleObserver {
    private String TAG;

    public LifecycleObserverComponent(String tag) {
        this.TAG = tag;
    }
    public void registerLifeCycle(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
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
