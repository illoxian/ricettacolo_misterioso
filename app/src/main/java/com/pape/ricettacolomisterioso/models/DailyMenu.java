package com.pape.ricettacolomisterioso.models;

import java.util.Date;
import java.util.List;

public class DailyMenu {

    private Date day;
    private List<DailyRecipe> recipes;

    public DailyMenu() {
    }

    public DailyMenu(Date day, List<DailyRecipe> recipes) {
        this.day = day;
        this.recipes = recipes;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<DailyRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<DailyRecipe> recipes) {
        this.recipes = recipes;
    }

    public void updateRecipe(int index, DailyRecipe dailyRecipe) {
        recipes.set(index, dailyRecipe);
    }

    @Override
    public String toString() {
        return "MenuOfTheDay{" +
                "day=" + day +
                ", recipes=" + recipes.toString() +
                '}';
    }
}
