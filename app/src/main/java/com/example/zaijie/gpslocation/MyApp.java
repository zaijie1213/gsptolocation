package com.example.zaijie.gpslocation;

import com.baidu.location.LocationClient;
import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by zaijie on 15/7/22.
 */
public class MyApp extends com.orm.SugarApp {

    public static LocationClient mLocationClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
        JuheSDKInitializer.initialize(this);
        mLocationClient = new LocationClient(this);
    }


}
