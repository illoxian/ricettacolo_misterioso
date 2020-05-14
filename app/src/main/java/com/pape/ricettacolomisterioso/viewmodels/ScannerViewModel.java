package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.ProductFromApi;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

public class ScannerViewModel extends ViewModel {

    private static final String TAG = "ScannerViewModel";

    private MutableLiveData<ProductFromApi> product;

    public MutableLiveData<ProductFromApi> getProduct() {
        if (product == null) {
            product = new MutableLiveData<>();
        }
        return product;
    }

    public MutableLiveData<ProductFromApi> getProductInfo(String code) {
        Log.d(TAG, "getProductInfo: Download the product info from Internet");
        ProductsRepository.getInstance().getProductInfo(getProduct(), code);
        return product;
    }

    public void clear(){
        product = null;
    }

}