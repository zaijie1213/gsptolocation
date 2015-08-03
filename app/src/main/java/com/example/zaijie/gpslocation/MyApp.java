package com.example.zaijie.gpslocation;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by zaijie on 15/7/22.
 */
public class MyApp extends Application {

    public static LocationClient mLocationClient = null;

    public static LocationClient getmLocationClient() {
        return mLocationClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JuheSDKInitializer.initialize(this);
        mLocationClient = new LocationClient(this);
    }


}
