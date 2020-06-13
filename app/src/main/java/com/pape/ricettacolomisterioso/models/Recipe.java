package com.pape.ricettacolomisterioso.models;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pape.ricettacolomisterioso.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String recipe_name;
    private String recipe_category;
    private List<String> ingredients;
    private List<String> steps;
    private String imageUrl;

    public Recipe () {}
    public Recipe(int id, String recipe_name, String imageUrl, String recipe_category, List<String> ingredients, List<String> steps) {
        this.id = id;
        this.recipe_name = recipe_name;
        this.recipe_category = recipe_category;

        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public void setRecipe_category(String recipe_category) {
        this.recipe_category = recipe_category;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipe_name='" + recipe_name + '\'' +
                ", recipe_category='" + recipe_category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }


    // parcelaber.com
    protected Recipe(Parcel in) {
        id = in.readInt();
        recipe_name = in.readString();
        recipe_category = in.readString();
        this.imageUrl = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<String>();
            in.readList(ingredients, String.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<String>();
            in.readList(steps, String.class.getClassLoader());
        } else {
            steps = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipe_name);
        dest.writeString(recipe_category);
        dest.writeString(this.imageUrl);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getCategoryIconId(Context context){
        Resources res = context.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.recipeCategoriesIcon);
        List categories = Arrays.asList(res.getStringArray(R.array.recipeCategoriesString));

        int index = categories.indexOf(getRecipe_category());
        int resourceId = icons.getResourceId(index, -1);
        icons.recycle();
        return resourceId;
    }

    public int getCategoryPreviewId(Context context){
        Resources res = context.getResources();
        TypedArray previews = res.obtainTypedArray(R.array.recipeCategoriesPreviews);
        List categories = Arrays.asList(res.getStringArray(R.array.recipeCategoriesString));

        int index = categories.indexOf(getRecipe_category());
        int resourceId = previews.getResourceId(index, -1);
        previews.recycle();
        return resourceId;
    }

}
