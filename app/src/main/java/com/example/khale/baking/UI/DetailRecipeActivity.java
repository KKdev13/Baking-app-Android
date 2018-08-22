package com.example.khale.baking.UI;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static com.example.khale.baking.Finals.*;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeActivity extends AppCompatActivity {

    private Recipe recipe;
    //private FragmentManager fragmentManager;
    private RecipeStepFragment recipeStepFragment;
    private FragmentManager fragmentManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        fragmentManager = getSupportFragmentManager();


        if(savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            recipe = bundle.getParcelable(SELECTED_RECIPE);

            final RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, recipeDetailFragment).addToBackStack(RECIPE_DETAIL)
                    .commit();

            if(RecipeFragment.isTablet && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                bundle.putParcelable(SELECTED_RECIPE, recipe);
                recipeStepFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, recipeStepFragment).addToBackStack(RECIPE_STEP)
                        .commit();
            }
        }else {
            recipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
        }

        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);

        recipeStepFragment = (RecipeStepFragment) fragmentManager.findFragmentByTag("RecipeStepFragment");

        if(recipeStepFragment != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && RecipeStepFragment.isPlaying){
            getSupportActionBar().hide();
        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            if(recipe != null){
                getSupportActionBar().setTitle(recipe.getName());
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_RECIPE, recipe);
    }

    public void goBack(){
        if(findViewById(R.id.fragment_container2) == null){
            if(fragmentManager.getBackStackEntryCount() > 1){
                fragmentManager.popBackStack(RECIPE_DETAIL, 0);
            }else if(fragmentManager.getBackStackEntryCount() > 0){
                finish();
            }
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
