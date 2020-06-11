package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.repositories.DailyMenuRepository;
import com.pape.ricettacolomisterioso.utils.Functions;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {

    public static String TAG = "NotificationReceiver";
    private NotificationHelper notificationHelper;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        int time = intent.getExtras().getInt("TIME");
        notificationHelper = new NotificationHelper(context);
        mContext = context;
        RetrieveDailyMenu(time);
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


        notificationHelper.createNotification(time, title, content, R.drawable.icon_categories_fish);

        Log.d(TAG, "onReceive: NotificationSended");
    }

    private String BuildContentString(String dish1, String dish2) {

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
