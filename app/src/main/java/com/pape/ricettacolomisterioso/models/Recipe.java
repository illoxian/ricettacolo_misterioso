package com.pape.ricettacolomisterioso.models;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pape.ricettacolomisterioso.R;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int categoryId;
    private List<String> ingredients;
    private List<String> steps;
    private Date saveDate;

    public Recipe() {}
    public Recipe(int id, String title, int categoryId, List<String> ingredients, List<String> steps, Date saveDate) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.ingredients = ingredients;
        this.steps = steps;
        this.saveDate = saveDate;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        categoryId = in.readInt();
        ingredients = in.createStringArrayList();
        steps = in.createStringArrayList();
        saveDate = new Date(in.readLong());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }
    public String getDateString(){
        if(saveDate != null)
            return DateFormat.getDateInstance(DateFormat.SHORT).format(saveDate);
        else return "";
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", saveDate=" + getDateString() +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(categoryId);
        dest.writeStringList(ingredients);
        dest.writeStringList(steps);
        dest.writeLong(saveDate==null ? 0 : saveDate.getTime());
    }

    public int getCategoryIconId(Context context){
        Resources res = context.getResources();
        TypedArray icons = res.obtainTypedArray(R.array.recipeCategoriesIcon);
        int index = getCategoryId();
        int resourceId = icons.getResourceId(index, -1);
        icons.recycle();
        return resourceId;
    }

    public int getCategoryPreviewId(Context context){
        Resources res = context.getResources();
        TypedArray previews = res.obtainTypedArray(R.array.recipeCategoriesPreviews);

        int index = getCategoryId();
        int resourceId = previews.getResourceId(index, -1);
        previews.recycle();
        return resourceId;
    }

}
