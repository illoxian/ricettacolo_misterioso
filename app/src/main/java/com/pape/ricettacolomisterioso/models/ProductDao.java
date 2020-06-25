package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    List<Product> getAll();

    //@Query("SELECT * FROM products WHERE uid IN (:userIds)")
    //List<Product> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM products WHERE product_name LIKE :first LIMIT 1")
    Product findByName(String first);

    @Query("SELECT * FROM products WHERE category LIKE :category")
    List<Product> findByCategory(int category);

    @Query("SELECT * FROM products ORDER BY expirationDate LIMIT 3")
    List<Product> getMostExpiringProduct();

    @Query("SELECT * FROM products ORDER BY expirationDate")
    List<Product> getProductOrderByExpirationDate();

    @Query("SELECT * FROM products WHERE product_name LIKE :product_name ||'%'")
    List<Product> getSearchedProducts(String product_name);

    @Query("SELECT * FROM products WHERE id LIKE :id LIMIT 1")
    Product findById(int id);

    @Query("UPDATE products SET quantity = quantity + 1 WHERE id LIKE :id")
    void plus(int id);

    @Query("UPDATE products SET quantity = quantity - 1 WHERE id LIKE :id")
    void minus(int id);

    @Update
    void update(Product product);

    @Insert
    void insertAll(Product... products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(Product product);

    @Delete
    int delete(Product product);

}


