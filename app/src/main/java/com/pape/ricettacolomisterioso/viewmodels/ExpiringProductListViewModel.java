package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

import java.util.List;

public class ExpiringProductListViewModel extends ViewModel {
    private static final String TAG = "ExpProductListViewModel";

    private MutableLiveData<List<Product>> products;

    public MutableLiveData<List<Product>> getMostExpiringProduct(){
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getMostExpiringProducts(products);
        return products;
    }

    public MutableLiveData<List<Product>> getAllProductsOrderByExpirationDate(){
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getMostExpiringProducts(products);
        return products;
    }
}
