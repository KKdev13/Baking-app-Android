package com.example.khale.baking.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khale.baking.Lambda;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.ItemViewHolder>{

    Context context;
    ArrayList<Recipe> recipes;
    Lambda<Integer> itemClickHandler;
    int selectedRecipeIndex = 0;

    public MainRecipeAdapter(Context context, Lambda<Integer> itemClickHandler){
        this.context = context;
        this.itemClickHandler = itemClickHandler;
    }

    public void setRecipes (ArrayList<Recipe> recipes, Context context){
        this.context = context;
        this.recipes = recipes;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int listItem = R.layout.recipe_item;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listItem, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, (i) -> itemClickHandler.execute(i));

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemViewHolder recipeItemViewHolder = (ItemViewHolder) holder;
        recipeItemViewHolder.text.setText(recipes.get(position).getName());
        String imageUrl = recipes.get(position).getImage();

        if(!TextUtils.isEmpty(imageUrl)){
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(context).load(builtUri).into(recipeItemViewHolder.image);
        }
        recipeItemViewHolder.updateIndex(position);

    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;

    }

    public Recipe getSelectedRecipe(int position){
        return recipes.get(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_text)
        TextView text;
        @BindView(R.id.iv_image)
        ImageView image;
        Lambda<Integer> clickHandler;
        int index;

        public ItemViewHolder(View itemView, Lambda<Integer> clickHandler){
            super(itemView);
            this.clickHandler = clickHandler;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this::click);
        }

        public void updateIndex(int index){
            this.index = index;
        }

        public void click(View view){
            notifyItemChanged(selectedRecipeIndex);
            selectedRecipeIndex = getAdapterPosition();
            notifyItemChanged(selectedRecipeIndex);

            clickHandler.execute(index);
        }

    }
}
