package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.repositories.ProductsRepository;

public class ScannerViewModel extends ViewModel {

    private static final String TAG = "ScannerViewModel";

    private MutableLiveData<Product> product;

    public MutableLiveData<Product> getProduct() {
        if (product == null) {
            product = new MutableLiveData<>();
        }
        return product;
    }

    public MutableLiveData<Product> getProductInfo(String code, String dataProvider) {
        Log.d(TAG, "getProductInfo: Download the product info from Internet");
        if(dataProvider.equals("ebay"))
            ProductsRepository.getInstance().getProductInfoEbay(getProduct(), code);
        else
            ProductsRepository.getInstance().getProductInfoOFF(getProduct(), code);
        return product;
    }

    public void clear(){
        product = null;
    }

}