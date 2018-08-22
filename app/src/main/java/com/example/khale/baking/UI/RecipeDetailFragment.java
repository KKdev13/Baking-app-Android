package com.example.khale.baking.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.example.khale.baking.Finals.*;

import com.example.khale.baking.Adapter.DetailRecipeAdapter;
import com.example.khale.baking.Model.Ingredient;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.R;
import com.example.khale.baking.Widget.BakingWidgetService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;
    DetailRecipeAdapter detailRecipeAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textView;

        if(savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
        }else {
            recipe = getArguments().getParcelable(SELECTED_RECIPE);
        }

        List<Ingredient> ingredients = recipe.getIngredients();
        View view = inflater.inflate(R.layout.recipe_detail_fragment, container,false);
        textView = (TextView) view.findViewById(R.id.tv_ingredients);

        for(Ingredient ingredient: ingredients){
            textView.append(ingredient.getIngredient() +"\n");
            textView.append("Quantity: " + ingredient.getQuantity().toString() +"\n");
            textView.append("Measure: " + ingredient.getMeasure() + "\n\n");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        detailRecipeAdapter = new DetailRecipeAdapter(this::clickStep);
        recyclerView.setAdapter(detailRecipeAdapter);
        detailRecipeAdapter.setRecipeSteps(recipe);

        BakingWidgetService.startActionRecipeUpdate(getActivity(), recipe);
        return view;
    }

    public void clickStep(Integer i){

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_STEP, detailRecipeAdapter.getSelectedStep(i));
        bundle.putParcelableArrayList(ALL_STEPS, (ArrayList) detailRecipeAdapter.getSteps());
        recipeStepFragment.setArguments(bundle);

        if(RecipeFragment.isTablet && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, recipeStepFragment).addToBackStack(RECIPE_STEP)
                    .commit();
        }else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, recipeStepFragment, "RecipeStepFragment").addToBackStack(RECIPE_STEP)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable(SELECTED_RECIPE, recipe);
    }
}
