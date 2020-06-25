package com.pape.ricettacolomisterioso.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "daily_recipes", primaryKeys = {"day", "slot"})
public class DailyRecipe {

    @NonNull
    private Date day;
    private String recipe;
    @Embedded
    private Recipe recipeComplex;
    @NonNull
    private int slot;

    public DailyRecipe() {
    }

    public DailyRecipe(Date day, String recipe, int slot) {
        this.day = day;
        this.recipe = recipe;
        this.slot = slot;
    }

    public DailyRecipe(Date day, Recipe recipeComplex, int slot) {
        this.day = day;
        this.recipeComplex = recipeComplex;
        this.slot = slot;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Recipe getRecipeComplex() {
        return recipeComplex;
    }

    public void setRecipeComplex(Recipe recipeComplex) {
        this.recipeComplex = recipeComplex;
    }

    @Override
    public String toString() {
        String recipeComplexString = null;
        if (recipeComplex != null) recipeComplexString = recipeComplex.toString();
        return "DailyRecipe{" +
                "day=" + day +
                ", recipe='" + recipe + '\'' +
                ", recipeComplex=" + recipeComplexString +
                ", slot=" + slot +
                '}';
    }

    public String getRecipeName() {
        if (recipeComplex != null) return recipeComplex.getTitle();
        else return recipe;
    }
}
