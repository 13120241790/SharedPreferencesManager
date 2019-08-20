package com.julive.sharedpreferencesmanager;

import android.app.Application;

import com.google.gson.Gson;
import com.julive.library.JsonParserStrategy;
import com.julive.library.SharedPreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.getInstance().init(this);
        SharedPreferencesManager.getInstance().setJsonParserStrategy(new JsonParserStrategy() {
            @Override
            public String encode(Object o) {
                String jsonString;
                Gson gson = new Gson();
                jsonString = gson.toJson(o);

                //fast
                //jsonString = JSON.toJSONString(o);
                return jsonString;
            }

            @Override
            public Object decode(String jsonString, Class<?> c) {
                if (jsonString == null) {
                    return null;
                }

                Object o;
                Gson gson = new Gson();
                o = gson.fromJson(jsonString, c);

                // fast
                // o = JSON.parseObject(jsonString, c);
                return o;
            }
        });
    }
}
