package com.example.khale.baking.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khale.baking.Lambda;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.Model.Step;
import com.example.khale.baking.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.ItemViewHolder>{

    List<Step> steps;
    String recipeName;
    int selectedStepIndex = 0;
    Lambda<Integer> itemClickHandler;

    public DetailRecipeAdapter(Lambda<Integer> itemClickHandler){
        this.itemClickHandler = itemClickHandler;
    }

    public void setRecipeSteps(Recipe recipe){
        steps = recipe.getSteps();
        recipeName = recipe.getName();
        notifyDataSetChanged();
    }

    public List<Step> getSteps(){
        return steps;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int listItem = R.layout.recipe_detail_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listItem, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, (i) -> itemClickHandler.execute(i));

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemViewHolder stepItemViewHolder = (ItemViewHolder) holder;
        stepItemViewHolder.step.setText(steps.get(position).getId() + ". " + steps.get(position).getShortDescription());

        stepItemViewHolder.updateIndex(position);

    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    public Step getSelectedStep(int position){
        return steps.get(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_step)
        TextView step;
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
            notifyItemChanged(selectedStepIndex);
            selectedStepIndex = getAdapterPosition();
            notifyItemChanged(selectedStepIndex);

            clickHandler.execute(index);
        }
    }
}
