package com.krinotech.bakingapp;

import com.google.gson.Gson;
import com.krinotech.bakingapp.model.Recipe;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RecipeInterceptor implements Interceptor {
    public final int SUCCESS_CODE = 200;
    public List<Recipe> recipes;

    public RecipeInterceptor(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString = new Gson().toJson(recipes);
        return chain.proceed(chain.request())
                .newBuilder()
                .code(SUCCESS_CODE)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(ResponseBody.create(MediaType.parse("application/json"),
                        responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
    }
}
