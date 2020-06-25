package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.EbayProductApiResponse;
import com.pape.ricettacolomisterioso.models.OFFProductApiResponse;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.services.EbayService;
import com.pape.ricettacolomisterioso.services.FoodService;
import com.pape.ricettacolomisterioso.ui.MainActivity;
import com.pape.ricettacolomisterioso.utils.Constants;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private EbayService ebayService;

    private ProductsRepository() {
        appDatabase = MainActivity.db;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.FOOD_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        TikXml tikxml = new TikXml.Builder().exceptionOnUnreadXml(false).build();
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl(Constants.EBAY_API_BASE_URL).
                addConverterFactory(TikXmlConverterFactory.create(tikxml)).build();

        foodService = retrofit.create(FoodService.class);
        ebayService = retrofit2.create(EbayService.class);
    }

    public static synchronized ProductsRepository getInstance() {
        if (instance == null) {
            instance = new ProductsRepository();
        }
        return instance;
    }

    public void addProduct(Product product, MutableLiveData<Long> insertId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.productDao().insertProduct(product);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
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

    public void getProductById(MutableLiveData<Product> product) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    product.postValue(appDatabase.productDao().findById(product.getValue().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getProductsByCategory(MutableLiveData<List<Product>> products, int category) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getProducts() " + category);
                    products.postValue(appDatabase.productDao().findByCategory(category));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getMostExpiringProducts(MutableLiveData<List<Product>> products) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getMostExpiringProducts() ");
                    products.postValue(appDatabase.productDao().getMostExpiringProduct());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getAllProductsOrderByExpirationDate(MutableLiveData<List<Product>> products) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getMostExpiringProducts() ");
                    products.postValue(appDatabase.productDao().getProductOrderByExpirationDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getProductSearched(MutableLiveData<List<Product>> products, String product_name) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getProductSearched() ");
                    products.postValue(appDatabase.productDao().getSearchedProducts(product_name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void delete(Product product, MutableLiveData<Integer> deleteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = appDatabase.productDao().delete(product);
                    Log.d(TAG, "run: deleteId:" + id);
                    deleteId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void plus(MutableLiveData<Product> product) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    appDatabase.productDao().plus(product.getValue().getId());
                    product.postValue(appDatabase.productDao().findById(product.getValue().getId()));
                    Log.d(TAG, "plus UPDATES" + appDatabase.productDao().findById(product.getValue().getId()).toString());
//                    appDatabase.productDao().update(product.getValue());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }


    public void minus(MutableLiveData<Product> product) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (appDatabase.productDao().findById(product.getValue().getId()).getQuantity() > 0) {
                        appDatabase.productDao().minus(product.getValue().getId());
                        product.postValue(appDatabase.productDao().findById(product.getValue().getId()));
                        Log.d(TAG, "minus UPDATES" + appDatabase.productDao().findById(product.getValue().getId()).toString());
//                        appDatabase.productDao().update(product.getValue());

                    } else {
                        Log.d(TAG, "quantity 0, cannot underflow");
                        product.postValue(appDatabase.productDao().findById(product.getValue().getId()));
                        Log.d(TAG, "minus UPDATES" + appDatabase.productDao().findById(product.getValue().getId()).toString());
//                        appDatabase.productDao().update(product.getValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }

    public void addItem(Product product, MutableLiveData<Long> insertId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.productDao().insertProduct(product);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    public void getProductInfoOFF(MutableLiveData<Product> product, String code) {
        Call<OFFProductApiResponse> call = foodService.getProductInfo(code, Constants.FOOD_API_USER_AGENT);
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<OFFProductApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<OFFProductApiResponse> call, @NotNull Response<OFFProductApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 1) { //API returns successful even if product was not found
                        Log.d(TAG, "onResponse: " + response.body().toString());
                        product.postValue(response.body().getProduct());
                    } else {
                        Log.d(TAG, "onResponse: Success. Product not found");
                        product.postValue(null);
                    }
                } else if (response.errorBody() != null) {
                    product.postValue(null);
                    try {
                        new Throwable(response.errorBody().string()).printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OFFProductApiResponse> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
            }
        });
    }

    public void getProductInfoEbay(MutableLiveData<Product> product, String code) {
        String requestBodyText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<FindProductsRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">"
                + "<ProductID type=\"EAN\">" + code + "</ProductID>"
                + "<MaxEntries>1</MaxEntries>"
                + "<AvailableItemsOnly>true</AvailableItemsOnly>"
                + "</FindProductsRequest>";
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), requestBodyText);
        Call<EbayProductApiResponse> call = ebayService.getProductInfo(requestBody, Constants.EBAY_API_APP_ID);
        Log.d(TAG, "prova:");
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<EbayProductApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<EbayProductApiResponse> call, @NotNull Response<EbayProductApiResponse> response) {
                Log.d(TAG, "onResponse:");
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 1) { //API returns successful even if product was not found
                        Log.d(TAG, "onResponse: " + response.body().toString());
                        product.postValue(response.body().getProduct());
                    } else {
                        Log.d(TAG, "onResponse: Success. Product not found");
                        product.postValue(null);
                    }
                } else if (response.errorBody() != null) {
                    Log.d(TAG, "onResponse: Error.");
                    try {
                        new Throwable(response.errorBody().string()).printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EbayProductApiResponse> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
            }
        });
    }
}
