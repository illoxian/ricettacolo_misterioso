package com.pape.ricettacolomisterioso.ui.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String productName;
    public String category;
    public Date expirationDate;
}
