package com.example.kindremind_mobileappproject.ui;

// executes before doing anything with activites. I just created to manage database opening/closign

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kindremind_mobileappproject.ui.adapters.DBAdapter;
import com.example.kindremind_mobileappproject.ui.utils.NotificationScheduler;

public class KindRemind extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBAdapter.getInstance(this).open();   // open once
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean vibrationEnabled = prefs.getBoolean(SettingsActivity.PREF_VIBRATION_ENABLED, true);

        if (vibrationEnabled) {
            NotificationScheduler.schedule(this);
        }
    }
}