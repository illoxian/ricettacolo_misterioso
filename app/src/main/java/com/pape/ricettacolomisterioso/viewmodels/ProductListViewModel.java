package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

import java.util.List;

public class ProductListViewModel extends ViewModel {
    private static final String TAG = "ProductListViewModel";

    private MutableLiveData<List<Product>> products;

    public MutableLiveData<List<Product>> getProductsByCategory(String category) {
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getProducts: getProductsUpdate");
        ProductsRepository.getInstance().getProductsByCategory(products, category);
        return products;
    }

}
