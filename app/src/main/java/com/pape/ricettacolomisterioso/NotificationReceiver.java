package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    public static String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        int time = intent.getExtras().getInt("TIME");

        String title;
        String content;
        if(time == 0) //launch
        {
            title = "Ora di pranzo!";
            content = "Per oggi hai pianificato di cucinare";
        }
        else{ //dinner
            title = "Ora di cena!";
            content = "Per oggi hai pianificato di cucinare";
        }
        notificationHelper.createNotification(time, title, content, R.drawable.icon_categories_fish);

        Log.d(TAG, "onReceive: NotificationSended");
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "StandardChannel")
                .setSmallIcon(R.drawable.icon_categories_fish)
                .setContentTitle("Text notifica")
                .setContentText("Un bel testo lungo per testare le notifiche")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());*/
    }

}
