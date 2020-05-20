package com.pape.ricettacolomisterioso.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pape.ricettacolomisterioso.utils.Converters;

@Database(entities = {Product.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}