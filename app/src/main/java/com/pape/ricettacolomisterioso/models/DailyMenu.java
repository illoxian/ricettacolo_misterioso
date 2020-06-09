package com.pape.ricettacolomisterioso.models;

import java.util.Date;
import java.util.List;

public class DailyMenu {

    private Date day;
    private List<String> recipes;

    public DailyMenu() {
    }

    public DailyMenu(Date day, List<String> recipes) {
        this.day = day;
        this.recipes = recipes;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "MenuOfTheDay{" +
                "day=" + day +
                ", recipes=" + recipes.toString() +
                '}';
    }
}
