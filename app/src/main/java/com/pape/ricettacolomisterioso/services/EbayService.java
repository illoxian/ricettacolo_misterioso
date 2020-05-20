package com.pape.ricettacolomisterioso.services;

import com.pape.ricettacolomisterioso.models.EbayProductApiResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * It represents the HTTP requests to use the API of https://newsapi.org.
 */
public interface EbayService {


    @Headers({
            "X-EBAY-API-SITE-ID:101",
            "X-EBAY-API-CALL-NAME:FindProducts",
            "X-EBAY-API-VERSION:863",
            "X-EBAY-API-REQUEST-ENCODING:xml"})
    @POST("shopping/")
    Call<EbayProductApiResponse> getProductInfo(@Body RequestBody requestBody,
                                                @Header("X-EBAY-API-APP-ID") String appId);

}
