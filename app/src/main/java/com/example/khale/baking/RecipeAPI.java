package com.example.khale.baking;

import com.example.khale.baking.Model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
