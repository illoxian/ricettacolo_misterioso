package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

import java.util.List;

public class ExpiringProductListViewModel extends ViewModel {
    private static final String TAG = "ExpProductListViewModel";

    private MutableLiveData<List<Product>> mostExpiringProducts;
    private MutableLiveData<List<Product>> expiringProductsOrdered;

    public MutableLiveData<List<Product>> getMostExpiringProduct(){
        if (mostExpiringProducts == null) {
            mostExpiringProducts = new MutableLiveData<>();
        }

        return mostExpiringProducts;
    }

    public MutableLiveData<List<Product>> getExpiringProductsOrdered(){
        if (expiringProductsOrdered == null) {
            expiringProductsOrdered = new MutableLiveData<>();
        }

        return expiringProductsOrdered;
    }

    public MutableLiveData<List<Product>> getAllMostExpiringProduct(){
        Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getMostExpiringProducts(mostExpiringProducts);
        return mostExpiringProducts;
    }

    public MutableLiveData<List<Product>> getAllProductsOrderByExpirationDate(){
         Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getAllProductsOrderByExpirationDate(expiringProductsOrdered);
        return expiringProductsOrdered;
    }
}
