package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public static String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification();

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
