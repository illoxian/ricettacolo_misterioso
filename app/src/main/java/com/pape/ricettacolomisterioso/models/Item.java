package com.pape.ricettacolomisterioso.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    boolean isSelected;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String itemName;
    private int quantity;

    public Item() {
    }

    public Item(String itemName, int itemCounter, boolean isSelected) {
        this.itemName = itemName;
        this.quantity = itemCounter;
        this.isSelected = isSelected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", isSelected=" + isSelected +
                '}';
    }
}
