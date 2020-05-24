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

    @Insert
    void insertAll(Item... items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(Item item);

    @Delete
    void delete(Item item);

}
