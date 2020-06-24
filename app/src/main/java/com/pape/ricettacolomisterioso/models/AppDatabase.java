package com.pape.ricettacolomisterioso.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pape.ricettacolomisterioso.utils.Converters;

@Database(entities = {Product.class, Item.class, Recipe.class, DailyRecipe.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract ItemDao itemDao();
    public abstract RecipeDao recipeDao();
    public abstract MenuDao menuDao();

    private static final String DB_NAME = "database-name";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();
        }
        return instance;
    }

}