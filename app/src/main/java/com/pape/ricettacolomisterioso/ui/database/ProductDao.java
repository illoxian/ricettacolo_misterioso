package com.pape.ricettacolomisterioso.ui.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM products WHERE uid IN (:userIds)")
    List<Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM products WHERE productName LIKE :first LIMIT 1")
    Product findByName(String first);

    @Insert
    void insertAll(Product... products);

    @Insert
    long insertProduct(Product product);

    @Delete
    void delete(Product product);
}


