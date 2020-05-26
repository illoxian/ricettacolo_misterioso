package com.pape.ricettacolomisterioso.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey
    private int id;

    private String recipe_name;
    private String recipe_category;
    private String recipe_brief;
    private List<String> ingredients;
    private List<String> steps;

    public Recipe () {}
    public Recipe(int id, String recipe_name, String recipe_category, String recipe_brief, List<String> ingredients, List<String> steps) {
        this.id = id;
        this.recipe_name = recipe_name;
        this.recipe_category = recipe_category;
        this.recipe_brief = recipe_brief;
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

    public String getRecipe_brief() {
        return recipe_brief;
    }

    public void setRecipe_brief(String recipe_brief) {
        this.recipe_brief = recipe_brief;
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
                ", recipe_brief='" + recipe_brief + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }


    // parcelaber.com
    protected Recipe(Parcel in) {
        id = in.readInt();
        recipe_name = in.readString();
        recipe_category = in.readString();
        recipe_brief = in.readString();
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
        dest.writeString(recipe_brief);
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


}
