package com.example.zaijie.gpslocation;

import android.app.Application;

import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by zaijie on 15/7/22.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JuheSDKInitializer.initialize(this);
    }
}
