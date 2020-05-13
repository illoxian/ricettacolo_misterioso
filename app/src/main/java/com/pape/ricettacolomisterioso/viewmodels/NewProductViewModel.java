package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

import java.util.List;

public class NewProductViewModel extends ViewModel {
    private static final String TAG = "NewProductViewModel";

    private MutableLiveData<List<Product>> products;

    public LiveData<List<Product>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getProducts: getProductsUpdate");
        ProductsRepository.getInstance().getProducts(products);
        return products;
    }
}

