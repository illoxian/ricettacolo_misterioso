package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.pape.ricettacolomisterioso.utils.Functions;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(sharedPreferences.getBoolean("notifications_launch_dinner", true))
                Functions.SetAlarmManager(context);
            else
                Functions.ClearAlarmManager(context);
        }
    }
}