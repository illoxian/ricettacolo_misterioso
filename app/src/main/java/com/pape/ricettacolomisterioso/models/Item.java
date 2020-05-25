package com.pape.ricettacolomisterioso.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey
    @NonNull
    private String itemName;
    private int itemCounter;

    public Item() {}

    public Item(String itemName, int itemCounter) {
        this.itemName = itemName;
        this.itemCounter = itemCounter;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemCounter() {
        return itemCounter;
    }

    public void setItemCounter(int itemCounter) {
        this.itemCounter = itemCounter;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
