package com.pape.ricettacolomisterioso.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface MenuDao {

        @Query("SELECT * FROM daily_menus")
        List<DailyMenu> getAll();

        @Query("SELECT * FROM daily_menus WHERE day>=:from AND day<:to")
        List<DailyMenu> getDailyMenusFromTo(Date from, Date to);

        @Query("SELECT * FROM daily_menus WHERE day=:day")
        DailyMenu getDailyMenu(Date day);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        long insert(DailyMenu dailyMenu);

        @Delete
        int delete(DailyMenu dailyMenu);

}
