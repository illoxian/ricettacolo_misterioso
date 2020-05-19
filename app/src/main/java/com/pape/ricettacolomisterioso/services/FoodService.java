package com.pape.ricettacolomisterioso.services;

import com.pape.ricettacolomisterioso.models.OFFProductApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * It represents the HTTP requests to use the API of https://newsapi.org.
 */
public interface FoodService {

    @GET("product/{code}")
    Call<OFFProductApiResponse> getProductInfo(@Path(value = "code", encoded = true) String code,
                                               @Header("User-Agent") String userAgent);

}
