package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.ui.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyMenuRepository {
    public static final String TAG = "DailyMenuRepository";

    private static DailyMenuRepository instance;
    private AppDatabase appDatabase;


    private DailyMenuRepository() {
        setDatabase(MainActivity.db);
    }

    public void setDatabase(AppDatabase db){
        appDatabase = db;
    }

    //this defines a singleton for ShoppingListRepository Class
    public static synchronized DailyMenuRepository getInstance() {
        if (instance == null) {
            instance = new DailyMenuRepository();
        }
        return instance;
    }

    public void getDailyMenus(MutableLiveData<List<DailyMenu>> dailyMenus, List<Date> days) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    Log.d(TAG, "run: getDailyMenus()");
                    List<DailyMenu> menus = new ArrayList<>();
                    for(int i = 0; i<days.size(); i++){
                        List<DailyRecipe> recipes = new ArrayList<>();
                        for(int j = 0; j<4; j++){
                            DailyRecipe recipe = appDatabase.menuDao().getDailyRecipes(days.get(i), j);

                            if(recipe != null) recipes.add(recipe);
                            else recipes.add(null);
                        }
                        menus.add(new DailyMenu(days.get(i), recipes));
                    }
                    dailyMenus.postValue(menus);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public DailyMenu getDailyMenuSync(Date day) {
        List<DailyRecipe> recipes = new ArrayList<>();
        for(int j = 0; j<4; j++){
            DailyRecipe recipe = appDatabase.menuDao().getDailyRecipes(day, j);

            if(recipe != null) recipes.add(recipe);
            else recipes.add(null);
        }
        return new DailyMenu(day, recipes);
    }

    public void insert(DailyRecipe dailyRecipe, MutableLiveData<Long> insertId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.menuDao().insert(dailyRecipe);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void delete(DailyRecipe dailyRecipe, MutableLiveData<Integer> deleteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = appDatabase.menuDao().delete(dailyRecipe);
                    deleteId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
