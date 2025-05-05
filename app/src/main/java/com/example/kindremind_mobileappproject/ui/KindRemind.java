package com.example.kindremind_mobileappproject.ui;

// executes before doing anything with activites. I just created to manage database opening/closign

import android.app.Application;

import com.example.kindremind_mobileappproject.ui.adapters.DBAdapter;

public class KindRemind extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBAdapter.getInstance(this).open();   // open once
    }
}