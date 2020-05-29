package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipes")
    List<Recipe> getAll();



    @Query("SELECT * FROM recipes WHERE recipe_name LIKE :first LIMIT 1")
    Recipe findByName(String first);

    @Query("SELECT * FROM recipes WHERE recipe_category LIKE :recipe_category")
    List<Recipe> findByCategory(String recipe_category);

    @Insert
    void insertAll(Recipe... recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);
}


