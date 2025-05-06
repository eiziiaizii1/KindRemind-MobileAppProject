package com.example.kindremind_mobileappproject.ui.utils;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import java.util.concurrent.TimeUnit;

public class NotificationScheduler {
    private static final String UNIQUE_WORK_NAME = "HourlyReminderWork";

    public static void schedule(Context context) {
         //// Production version that sends notification hourly
       /*PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(

                NotificationWorker.class,
                1, TimeUnit.HOURS //-> hourly notification
               //15, TimeUnit.MINUTES // -> every 15 minutes
               // 6, TimeUnit.HOURS ->  6-hour notification

        ).build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
        );*/

        //  TEST VERSION
        OneTimeWorkRequest testRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(20, TimeUnit.SECONDS)
                .build();

        //WorkManager.getInstance(context).enqueue(testRequest);
        WorkManager.getInstance(context).enqueueUniqueWork(
                "KindRemindTestWork",
                ExistingWorkPolicy.REPLACE,
                testRequest
        );

    }

    public static void cancel(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME);
    }
}