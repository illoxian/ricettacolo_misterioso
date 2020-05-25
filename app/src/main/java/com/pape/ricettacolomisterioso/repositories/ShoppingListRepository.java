package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.ui.MainActivity;

import java.util.List;

public class ShoppingListRepository {
    public static final String TAG="ShoppingListRepository";

    private static ShoppingListRepository instance;
    private AppDatabase appDatabase;


    private ShoppingListRepository() {
        appDatabase = MainActivity.db;


    }
    //this defines a singleton for ShoppingListRepository Class
    public static synchronized ShoppingListRepository getInstance() {
        if (instance == null) {
            instance = new ShoppingListRepository();
        }
        return instance;
    }


    public void getItems(MutableLiveData<List<Item>> items) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getItems()");
                    items.postValue(appDatabase.itemDao().getAll());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }




}
