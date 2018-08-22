package com.example.khale.baking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import static com.example.khale.baking.Finals.*;



public class RetrofitBuilder {

    static RecipeAPI recipeAPI;
    //private static String baseUrl= "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    public static RecipeAPI getData(){
        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        recipeAPI = new Retrofit.Builder()
                .baseUrl(Finals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(RecipeAPI.class);

        return recipeAPI;

    }
}
