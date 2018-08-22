package com.example.khale.baking.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private String ingredient;
    private Double quantity;
    private String measure;

    //getters and setters
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity){
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure){
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //constructors
    protected Ingredient(Parcel parcel){
        this.ingredient = parcel.readString();
        this.quantity = parcel.readDouble();
        this.measure = parcel.readString();
    }
    public Ingredient(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ingredient);
        dest.writeDouble(this.quantity);
        dest.writeString(this.measure);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
