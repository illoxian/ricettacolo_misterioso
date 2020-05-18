package com.pape.ricettacolomisterioso.models;

import java.util.Locale;

public class ProductFromApi extends Product {
    private String generic_name_it;
    private String generic_name_en;
    private String product_name_it;
    private String product_name_en;
    private String brands;
    private String image_url;
    private String code;

    public ProductFromApi(){

    }

    public ProductFromApi(String generic_name_it, String generic_name_en, String product_name_it, String product_name_en, String brands, String image_url, String code) {
        this.generic_name_it = generic_name_it;
        this.generic_name_en = generic_name_en;
        this.product_name_it = product_name_it;
        this.product_name_en = product_name_en;
        this.brands = brands;
        this.image_url = image_url;
        this.code = code;
    }

    public String getGeneric_name_it() {
        return generic_name_it;
    }

    public void setGeneric_name_it(String generic_name_it) {
        this.generic_name_it = generic_name_it;
    }

    public String getGeneric_name_en() {
        return generic_name_en;
    }

    public void setGeneric_name_en(String generic_name_en) {
        this.generic_name_en = generic_name_en;
    }

    public String getProduct_name_it() {
        return product_name_it;
    }

    public void setProduct_name_it(String product_name_it) {
        this.product_name_it = product_name_it;
    }

    public String getProduct_name_en() {
        return product_name_en;
    }

    public void setProduct_name_en(String product_name_en) {
        this.product_name_en = product_name_en;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGeneric_name(){
        String lang = Locale.getDefault().getLanguage();
        if(lang == "it") return generic_name_it;
        else return generic_name_en;
    }

    public String getProduct_name(){
        String lang = Locale.getDefault().getLanguage();
        if(lang == "it") return product_name_it;
        else return product_name_en;
    }
}
