package com.pape.ricettacolomisterioso.ui.new_product;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.ui.database.Product;

import java.util.List;

public class NewProductViewModel extends ViewModel {
    private MutableLiveData<List<Product>> products;

    public MutableLiveData<List<Product>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<List<Product>>();
        }
        return products;
    }
}

