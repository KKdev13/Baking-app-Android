package com.example.khale.baking.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private Integer id;
    private String name;
    private Integer servings;
    private String image;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    //getters and setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps){
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }
    public void setServings(Integer servings){
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }


    //constructors
    protected Recipe(Parcel parcel){
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.ingredients = new ArrayList<>();
        parcel.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<>();
        parcel.readList(this.steps, Step.class.getClassLoader());
        this.servings = parcel.readInt();
        this.image = parcel.readString();
    }
    public Recipe(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public static final Parcelable.Creator<Recipe>CREATOR = new Parcelable.Creator<Recipe>(){
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
