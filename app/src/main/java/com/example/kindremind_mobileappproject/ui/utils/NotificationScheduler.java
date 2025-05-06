package com.example.kindremind_mobileappproject.ui.utils;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import java.util.concurrent.TimeUnit;

public class NotificationScheduler {
    private static final String UNIQUE_WORK_NAME = "HourlyReminderWork";

    public static void schedule(Context context) {
         //// Production version that sends notification hourly
       PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(

                NotificationWorker.class,
                1, TimeUnit.HOURS
        ).build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
        );

/*
        //  TEST VERSION (run after 10 seconds, then done — no repeat)
        OneTimeWorkRequest testRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(20, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(context).enqueue(testRequest);
*/
    }

    public static void cancel(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME);
    }
}