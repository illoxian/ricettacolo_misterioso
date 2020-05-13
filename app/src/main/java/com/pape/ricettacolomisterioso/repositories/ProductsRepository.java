package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.ui.MainActivity;

import java.util.List;

public class ProductsRepository {

    private static final String TAG = "ProductsRepository";

    private static ProductsRepository instance;
    private AppDatabase appDatabase;

    private ProductsRepository() {
        appDatabase = MainActivity.db;
    }

    public static synchronized ProductsRepository getInstance() {
        if (instance == null) {
            instance = new ProductsRepository();
        }
        return instance;
    }

    public void getProducts(MutableLiveData<List<Product>> products) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getProducts()");
                    products.postValue(appDatabase.productDao().getAll());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }
}
