package com.pape.ricettacolomisterioso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pape.ricettacolomisterioso.utils.Functions;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Functions.SetAlarmManager(context);
        }
    }
}