package com.julive.sharedpreferencesmanager;

import android.app.Application;

import com.julive.library.SharedPreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.getInstance().init(this);
    }
}
