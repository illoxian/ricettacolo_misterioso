package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Query("SELECT * FROM items WHERE itemName=:itemName")
    Item findItemInShoppingList(String itemName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(Item item);

    @Query("UPDATE items SET isSelected=:isSelected WHERE id = :id")
    int updateIsSelected(long id, Boolean isSelected);

    @Delete
    int delete(Item item);

    @Query("DELETE FROM items WHERE itemName = :itemName")
    int delete(String itemName);

}
