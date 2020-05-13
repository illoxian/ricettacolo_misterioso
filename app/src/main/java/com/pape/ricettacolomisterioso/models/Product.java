package com.pape.ricettacolomisterioso.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey @NotNull
    private String productName;

    private String category;
    private Date expirationDate;

    public Product(String productName, String category, Date expirationDate) {
        this.productName = productName;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", expirationDate=" + DateFormat.getDateInstance(DateFormat.SHORT).format(expirationDate) +
                '}';
    }
}
