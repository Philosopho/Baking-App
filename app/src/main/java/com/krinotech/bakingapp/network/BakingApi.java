package com.krinotech.bakingapp.network;

import com.krinotech.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> listRecipes();

}
