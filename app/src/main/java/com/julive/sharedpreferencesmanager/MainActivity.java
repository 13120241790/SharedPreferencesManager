package com.julive.sharedpreferencesmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.julive.library.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferencesManager.getInstance().put("keyString", "value");
        SharedPreferencesManager.getInstance().put("keyInt", 1);
        SharedPreferencesManager.getInstance().put("keyBoolean", true);

        String s = (String) SharedPreferencesManager.getInstance().get("keyString", "x");
        int i = (int) SharedPreferencesManager.getInstance().get("keyInt", 0);
        boolean b = (boolean) SharedPreferencesManager.getInstance().get("keyBoolean", false);
        Log.e(SharedPreferencesManager.class.getSimpleName(), "String : " + s + " int : " + i + " boolean : " + b);

    }

}

