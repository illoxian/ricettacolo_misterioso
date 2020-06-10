package com.pape.ricettacolomisterioso.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pape.ricettacolomisterioso.DeviceBootReceiver;
import com.pape.ricettacolomisterioso.NotificationReceiver;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.AppDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static AppDatabase db;
    private static PendingIntent pendingIntent;

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
            SetAlarmManager(this);
        else
            ClearAlarmManager(this);

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

        /*Intent intent = new Intent(this, Product_profile.class);
        startActivity(intent);*/

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

    public static void SetAlarmManager(Context context)
    {
        //enable boot receiver
        ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        //set hour of notification
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);

        //set alarm manager
        Intent intent = new Intent(context, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void ClearAlarmManager(Context context)
    {
        //disable boot receiver
        ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        //clear alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
