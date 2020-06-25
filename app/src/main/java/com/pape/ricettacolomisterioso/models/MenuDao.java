package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;

@Dao
public interface MenuDao {

    @Query("SELECT * FROM daily_recipes WHERE day=:day AND slot=:slot")
    DailyRecipe getDailyRecipes(Date day, int slot);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DailyRecipe dailyRecipe);

    @Delete
    int delete(DailyRecipe dailyRecipe);
}
