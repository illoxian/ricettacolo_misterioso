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

    public void addItem(Item item, MutableLiveData<Long> insertId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.itemDao().insertItem(item);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void updateIsSelected(long itemId, boolean isSelected, MutableLiveData<Integer> updateId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = appDatabase.itemDao().updateIsSelected(itemId, isSelected);
                    Log.d(TAG, "run: updateId:"+id);
                    updateId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void delete(String itemName, MutableLiveData<Integer> deleteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = appDatabase.itemDao().delete(itemName);
                    Log.d(TAG, "run: deleteId:"+id);
                    deleteId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void findItemFromName(String itemName, MutableLiveData<Item> item) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Item returnItem = appDatabase.itemDao().findItemInShoppingList(itemName);
                    item.postValue(returnItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
