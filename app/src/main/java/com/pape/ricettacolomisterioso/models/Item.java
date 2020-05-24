package com.pape.ricettacolomisterioso.models;

import androidx.room.Entity;

@Entity(tableName = "items")
public class Item {
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
