package com.pape.ricettacolomisterioso.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "Product")
public class EbayProductFromApi{
    @PropertyElement(name = "DisplayStockPhotos")
    private boolean displayStockPhotos;
    @PropertyElement(name = "StockPhotoURL")
    private String stockPhotoURL;
    @PropertyElement(name = "Title")
    private String title;

    public EbayProductFromApi(){

    }

    public EbayProductFromApi(boolean displayStockPhotos, String stockPhotoURL, String title) {
        this.displayStockPhotos = displayStockPhotos;
        this.stockPhotoURL = stockPhotoURL;
        this.title = title;
    }

    public boolean isDisplayStockPhotos() {
        return displayStockPhotos;
    }

    public void setDisplayStockPhotos(boolean displayStockPhotos) {
        this.displayStockPhotos = displayStockPhotos;
    }

    public String getStockPhotoURL() {
        return stockPhotoURL;
    }

    public void setStockPhotoURL(String stockPhotoURL) {
        this.stockPhotoURL = stockPhotoURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "EbayProductFromApi{" +
                "displayStockPhotos=" + displayStockPhotos +
                ", stockPhotoURL='" + stockPhotoURL + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}