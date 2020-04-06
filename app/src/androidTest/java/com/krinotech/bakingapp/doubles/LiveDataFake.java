package com.krinotech.bakingapp.doubles;

import androidx.lifecycle.LiveData;

public class LiveDataFake<T> extends LiveData<T> {

    public LiveDataFake(T t) {
        super(t);
    }
}
