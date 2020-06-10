package com.pape.ricettacolomisterioso.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.utils.Functions;


public class MainActivity extends AppCompatActivity {

    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check theme
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("dark_mode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //check notifications enabled
        if(sharedPreferences.getBoolean("notifications_launch_dinner", true))
            Functions.SetAlarmManager(this);
        else
            Functions.ClearAlarmManager(this);

        //createNotificationChannel();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_recipes, R.id.navigation_pantry, R.id.navigation_shoppinglist, R.id.navigation_menu, R.id.navigation_preferences)
                    .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SetDatabase();
    }

    private void SetDatabase(){

        if(db == null){
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
        }

    }

    /*private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canale notifiche standard";
            String description = "Un normale canale di notifiche";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("StandardChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

}
