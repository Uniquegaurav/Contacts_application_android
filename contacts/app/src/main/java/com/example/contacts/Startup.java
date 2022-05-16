package com.example.contacts;

import android.app.Application;
import android.content.SharedPreferences;

public class Startup extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences.Editor editor = getSharedPreferences("FIRST_START", MODE_PRIVATE).edit();
        editor.putBoolean("firstStart", true);
        editor.apply();
    }
}
