package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.models.ProductApiResponse;
import com.pape.ricettacolomisterioso.models.ProductFromApi;
import com.pape.ricettacolomisterioso.services.FoodService;
import com.pape.ricettacolomisterioso.ui.MainActivity;
import com.pape.ricettacolomisterioso.utils.Constants;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsRepository {

    private static final String TAG = "ProductsRepository";

    private static ProductsRepository instance;
    private AppDatabase appDatabase;
    private FoodService foodService;

    private ProductsRepository() {
        appDatabase = MainActivity.db;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.FOOD_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        foodService = retrofit.create(FoodService.class);
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

    public void getProductInfo(MutableLiveData<ProductFromApi> product, String code) {
        Call<ProductApiResponse> call = foodService.getProductInfo(code, Constants.FOOD_API_USER_AGENT);
        Log.d(TAG, "prova: " );
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<ProductApiResponse>() {
            @Override
            public void onResponse(Call<ProductApiResponse> call, Response<ProductApiResponse> response) {
                Log.d(TAG, "successful: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    product.postValue(response.body().getProduct());
                } else if (response.errorBody() != null) {
                    try {
                        new Throwable(response.errorBody().string()).printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductApiResponse> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
            }
        });
    }
}
