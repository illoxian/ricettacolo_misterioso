package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.repositories.DailyMenuRepository;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    public static String TAG = "NotificationReceiver";
    private NotificationHelper notificationHelper;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private long now;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: alarm manager fired");

        int time = intent.getExtras().getInt("TIME");

        /*sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        now = Functions.ExcludeTime(Calendar.getInstance().getTime()).getTime();

        // recupera l'ultima volta che è stata chiamata la notifica
        long last_fire;

        if(time==0) last_fire = sharedPreferences.getLong("notifications_launch_last_fire", 0);
        else last_fire = sharedPreferences.getLong("notifications_dinner_last_fire", 0);

        // se non è mai stata chiamata allora imposta oggi come ultimo giorno
        if(last_fire==0){
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong("notifications_launch_last_fire", now);
            edit.putLong("notifications_dinner_last_fire", now);
            edit.apply();
            last_fire = now;
        }

        // se oggi non è stata chiamata allora chiamala
        if(now>last_fire){*/
            notificationHelper = new NotificationHelper(context);
            mContext = context;
            DailyMenuRepository.getInstance().setDatabase(AppDatabase.getInstance(context));
            RetrieveDailyMenu(time);
        /*}
        else
        {
            Log.d(TAG, "onReceive: Notification not sended");
        }*/
    }

    private void RetrieveDailyMenu(int time) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    DailyMenu dailyMenu = DailyMenuRepository.getInstance().getDailyMenuSync(Functions.ExcludeTime(Calendar.getInstance().getTime()));
                    CreateNotification(time, dailyMenu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void CreateNotification(int time, DailyMenu dailyMenu){
        String title;
        String content;
        if(time == 0) //launch
        {
            title = mContext.getString(R.string.notification_lunch_time);
            content = BuildContentString(dailyMenu.getRecipes().get(0), dailyMenu.getRecipes().get(1));
        }
        else{ //dinner
            title = mContext.getString(R.string.notification_dinner_time);
            content = BuildContentString(dailyMenu.getRecipes().get(2), dailyMenu.getRecipes().get(3));
        }


        notificationHelper.createNotification(time, title, content, R.drawable.chef_notification);

        /*SharedPreferences.Editor edit = sharedPreferences.edit();
        if(time==0)
            edit.putLong("notifications_launch_last_fire", now);
        else
            edit.putLong("notifications_dinner_last_fire", now);
        edit.apply();*/

        Log.d(TAG, "onReceive: NotificationSended");
    }

    private String BuildContentString(DailyRecipe recipe1, DailyRecipe recipe2) {

        String dish1 = null, dish2 = null;
        if(recipe1!=null) dish1 = recipe1.getRecipeName();
        if(recipe2!=null) dish2 = recipe2.getRecipeName();

        if(dish1 == null && dish2 == null)
            return mContext.getString(R.string.notification_scheduled_recipes_0);
        else if(dish1 != null && dish2 == null)
            return mContext.getString(R.string.notification_scheduled_recipes_1, dish1);
        else if(dish1 == null && dish2 != null)
            return mContext.getString(R.string.notification_scheduled_recipes_1, dish2);
        else
            return mContext.getString(R.string.notification_scheduled_recipes_2, dish1, dish2);
    }

}
