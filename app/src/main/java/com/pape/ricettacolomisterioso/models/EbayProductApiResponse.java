package com.pape.ricettacolomisterioso.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "FindProductsResponse")
public class EbayProductApiResponse {
    @PropertyElement(name = "Ack")
    String ack;
    @Element(name = "Product")
    EbayProductFromApi product;

    public EbayProductApiResponse() {
    }

    public EbayProductApiResponse(String ack, EbayProductFromApi product) {
        this.ack = ack;
        this.product = product;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public Product getProduct() {
        Product p = new Product();
        p.setProduct_name(product.getTitle());
        p.setImageUrl(product.getStockPhotoURL());
        p.setDataSource("Ebay");
        return p;
    }

    public void setProduct(EbayProductFromApi product) {
        this.product = product;
    }

    public int getStatus(){
        if(ack.equals("Success")) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "EbayProductApiResponse{" +
                "ack='" + ack + '\'' +
                ", product=" + product +
                '}';
    }
}
