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

    @Query("SELECT * FROM recipes WHERE title LIKE :title ||'%'")
    List<Recipe> getSearchedRecipes(String title);

    @Query("SELECT * FROM recipes WHERE title LIKE :first LIMIT 1")
    Recipe findByName(String first);

    @Query("SELECT * FROM recipes WHERE categoryId LIKE :categoryId")
    List<Recipe> findByCategory(int categoryId);

    @Query("SELECT * FROM recipes ORDER BY RANDOM() LIMIT 1")
    Recipe getRand();



    @Insert
    void insertAll(Recipe... recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Delete
    int delete(Recipe recipe);
}


