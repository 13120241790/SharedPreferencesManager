package com.julive.sharedpreferencesmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.julive.library.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User();
        user.name = "张三";
        user.age = 30;
        user.address = "北京市东城区银河SOHO";
        user.sex = "男";
        user.phone = "110";
        user.isVip = true;

        SharedPreferencesManager.getInstance().put("currentUser", user);

        User cacheUser = (User) SharedPreferencesManager.getInstance().get("currentUser", new User());
        Log.e(SharedPreferencesManager.class.getSimpleName(), cacheUser.toString());

    }

    class User {
        String name;
        int age;
        String address;
        String sex;
        String phone;
        boolean isVip;

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", sex='" + sex + '\'' +
                    ", phone='" + phone + '\'' +
                    ", isVip=" + isVip +
                    '}';
        }
    }
}

