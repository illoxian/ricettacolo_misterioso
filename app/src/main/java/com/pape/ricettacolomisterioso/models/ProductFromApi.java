package com.pape.ricettacolomisterioso.models;

import java.util.Date;

public class ProductFromApi extends Product {
    private String generic_name_it;
    private String generic_name_en;
    private String product_name_it;
    private String product_name_en;
    private String brands;
    private String image_url;
    private String code;

    public ProductFromApi(String product_name, String category, Date expirationDate, String generic_name_it, String generic_name_en, String product_name_it, String product_name_en, String brands, String image_url, String code, String generic_name) {
        super(product_name, category, expirationDate);
        this.generic_name_it = generic_name_it;
        this.generic_name_en = generic_name_en;
        this.product_name_it = product_name_it;
        this.product_name_en = product_name_en;
        this.brands = brands;
        this.image_url = image_url;
        this.code = code;
        this.generic_name = generic_name;
    }

    private String generic_name;
    //private String product_name;


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

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    @Override
    public String toString() {
        return super.toString() + "ProductFromApi{" +
                "generic_name_it='" + generic_name_it + '\'' +
                ", generic_name_en='" + generic_name_en + '\'' +
                ", product_name_it='" + product_name_it + '\'' +
                ", product_name_en='" + product_name_en + '\'' +
                ", brands='" + brands + '\'' +
                ", image_url='" + image_url + '\'' +
                ", code='" + code + '\'' +
                ", generic_name='" + generic_name + '\'' +
                '}';
    }
}
