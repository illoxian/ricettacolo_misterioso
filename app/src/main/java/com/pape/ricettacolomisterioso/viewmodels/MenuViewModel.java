package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.DailyMenuRepository;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuViewModel extends ViewModel {
    private final String TAG= "MenuViewModel";

    private int weekOffset;

    private MutableLiveData<List<DailyMenu>> dailyMenus;
    private MutableLiveData<Long> insertId;
    private MutableLiveData<List<Recipe>> recipes;

    public MenuViewModel() {
        weekOffset = 0;
    }

    public MutableLiveData<List<DailyMenu>> getDailyMenus() {
        if (dailyMenus == null) {
            dailyMenus = new MutableLiveData<>();
        }
        return dailyMenus;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }
        return recipes;
    }

    public void ChangeWeek(int offset){
        weekOffset += offset;
        getWeekDailyMenus();
    }

    private List<Date> getWeekDays(){
        Calendar c1 = Calendar.getInstance();
        int weekOfYear = c1.get(Calendar.WEEK_OF_YEAR);

        weekOfYear += weekOffset;
        c1.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        List<Date> days = new ArrayList<>();
        for(int i=0; i<7; i++){
            int day = ((Calendar.MONDAY + 7 + i - 1) % 7)+1;
            c1.set(Calendar.DAY_OF_WEEK, day);
            days.add(Functions.ExcludeTime(c1.getTime()));
        }
        return days;
    }

    public String getWeekRangeString() {
        SimpleDateFormat format = new SimpleDateFormat("d MMM", Locale.getDefault());

        List<Date> days = getWeekDays();

        if (days.size() > 0) {
            String startDayString = format.format(days.get(0).getTime());
            String endDayString = format.format(days.get(days.size()-1).getTime());
            return startDayString + " - " + endDayString;
        }
        return null;
    }

    private void getWeekDailyMenus(){

        List<Date> days = getWeekDays();
        List<DailyMenu> menus = new ArrayList<>();

        DailyMenuRepository.getInstance().getDailyMenus(dailyMenus, days);
    }

    public void insert(DailyRecipe dailyRecipe){
        DailyMenuRepository.getInstance().insert(dailyRecipe, getInsertId());
    }

    public void getAllRecipes(){
        RecipesRepository.getInstance().getRecipes(recipes);
    }
}
