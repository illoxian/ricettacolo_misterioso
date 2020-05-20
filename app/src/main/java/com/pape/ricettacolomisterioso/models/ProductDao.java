package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    List<Product> getAll();

    //@Query("SELECT * FROM products WHERE uid IN (:userIds)")
    //List<Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM products WHERE product_name LIKE :first LIMIT 1")
    Product findByName(String first);

    @Query("SELECT * FROM products WHERE category LIKE :category LIMIT 1")
    List<Product> findByCategory(String category);

    @Insert
    void insertAll(Product... products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(Product product);

    @Delete
    void delete(Product product);
}


