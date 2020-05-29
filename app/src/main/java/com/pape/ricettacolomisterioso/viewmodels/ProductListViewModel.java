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

        Log.d(TAG, "getProductsByCategory: getProductsUpdate");
        ProductsRepository.getInstance().getProductsByCategory(products, category);
        return products;
    }

    public MutableLiveData<List<Product>> getAllProducts(){
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getProducts(products);
        return products;
    }

    public MutableLiveData<List<Product>> getProductsSearched(String product_name){
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getAllProducts: getProductsUpdate");
        ProductsRepository.getInstance().getProductSearched(products, product_name);
        return products;
    }
}
