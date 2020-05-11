package com.pape.ricettacolomisterioso.ui.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String productName;
    public String category;
}
