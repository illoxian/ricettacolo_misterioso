package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

public class NewProductViewModel extends ViewModel {
    private static final String TAG = "NewProductViewModel";

    private MutableLiveData<Long> insertId;

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<Long>();
        }
        return insertId;
    }


    /*
    per ora non utilizzato ma potrebbe servire

    private MutableLiveData<List<Product>> products;

    public LiveData<List<Product>> getProducts() {
        if (products == null) {
            products = new MutableLiveData<>();
        }

        Log.d(TAG, "getProducts: getProductsUpdate");
        ProductsRepository.getInstance().getProducts(products);
        return products;
    }

    */

    public MutableLiveData<Long> addProduct(Product product){
        ProductsRepository.getInstance().addProduct(product, getInsertId());
        return insertId;
    }
}

