package com.example.khale.baking.UI;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.khale.baking.Adapter.DetailRecipeAdapter;
import com.example.khale.baking.Adapter.MainRecipeAdapter;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.R;
import com.example.khale.baking.RecipeAPI;
import com.example.khale.baking.RetrofitBuilder;
import static com.example.khale.baking.Finals.*;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {

    //public static String ALL_RECIPES = "all_recipes";
    //public static String SELECTED_RECIPE = "selected_recipe";
    MainRecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipes;
    public static boolean isTablet;

    @BindView(R.id.recipe_recycler)
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        ButterKnife.bind(this, view);
        recipeAdapter = new MainRecipeAdapter(getActivity(), this::clickRecipe);
        recyclerView.setAdapter(recipeAdapter);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if(RecipeFragment.isTablet){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        if(savedInstanceState != null && savedInstanceState.getParcelableArrayList(ALL_RECIPES) != null){
            recipes = savedInstanceState.getParcelableArrayList(ALL_RECIPES);
            recipeAdapter.setRecipes(recipes, getContext());
        }else {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.loading_dialog));
            progressDialog.show();

            RecipeAPI recipeAPI = RetrofitBuilder.getData();
            Call<ArrayList<Recipe>> recipe = recipeAPI.getRecipe();

            if(isNetworkAvailable()){
                recipe.enqueue(new Callback<ArrayList<Recipe>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        recipes = response.body();
                        Bundle recipesBundle = new Bundle();
                        recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);
                        recipeAdapter.setRecipes(recipes, getContext());

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), "An error has occured!", Toast.LENGTH_SHORT);
                    }
                });
            }
        }
        return view;
    }

    public void clickRecipe(Integer i){
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_RECIPE, recipeAdapter.getSelectedRecipe(i));

        Intent intent = new Intent(getActivity(), DetailRecipeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ALL_RECIPES, recipes);
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
