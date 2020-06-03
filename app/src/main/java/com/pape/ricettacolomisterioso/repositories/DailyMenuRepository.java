package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.ui.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyMenuRepository {
    public static final String TAG = "DailyMenuRepository";

    private static DailyMenuRepository instance;
    private AppDatabase appDatabase;


    private DailyMenuRepository() {
        appDatabase = MainActivity.db;


    }

    //this defines a singleton for ShoppingListRepository Class
    public static synchronized DailyMenuRepository getInstance() {
        if (instance == null) {
            instance = new DailyMenuRepository();
        }
        return instance;
    }


    public void getDailyMenusFromTo(MutableLiveData<List<DailyMenu>> dailyMenus, List<Date> days) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getDailyMenus()");
                    List<DailyMenu> menus = new ArrayList<>();
                    for(int i = 0; i<days.size(); i++){
                        DailyMenu dm = appDatabase.menuDao().getDailyMenu(days.get(i));
                        if(dm == null)
                            dm = new DailyMenu(days.get(i), new ArrayList<>());
                        menus.add(dm);
                    }
                    dailyMenus.postValue(menus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void insert(DailyMenu dailyMenu, MutableLiveData<Long> insertId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.menuDao().insert(dailyMenu);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
