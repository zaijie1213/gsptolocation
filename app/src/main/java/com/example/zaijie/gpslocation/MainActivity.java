package com.example.zaijie.gpslocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public BDLocationListener listener;
    public LocationClient client;
    @Bind(R.id.lat)
    EditText mLat;
    @Bind(R.id.lng)
    EditText mLng;
    @Bind(R.id.btn_search)
    Button mBtnSearch;
    @Bind(R.id.result)
    TextView mResult;
    @Bind(R.id.getCurLoc)
    Button mGetCurLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UmengUpdateAgent.update(this);
        initLocation();
    }

    private void initLocation() {
        client = MyApp.getmLocationClient();
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String lati = String.valueOf(bdLocation.getLatitude());
                String langti = String.valueOf(bdLocation.getLongitude());
                mLng.setText(langti);
                mLat.setText(lati);
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 5000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        client.setLocOption(option);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @OnClick(R.id.btn_search)
    public void Search() {
        mBtnSearch.setEnabled(false);
        mResult.setText("`");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "查询中", "正在请求数据", false, false);
        dialog.show();

        String lat = mLat.getText().toString();
        String lng = mLng.getText().toString();
        if ((!TextUtils.isEmpty(lat)) && (!TextUtils.isEmpty(lng))) {
            Parameters parameters = new Parameters();
            parameters.add("lat", lat);
            parameters.add("lng", lng);

            JuheData.executeWithAPI(MainActivity.this, 15, "http://apis.juhe.cn/geo/", JuheData.GET, parameters, new DataCallBack() {
                @Override
                public void onSuccess(int i, String s) {
                    try {
                        mBtnSearch.setEnabled(true);
                        dialog.dismiss();
                        Log.d("szg", s);
                        JSONObject object = new JSONObject(s);
                        String addr = object.getJSONObject("result").getString("address");
                        if (TextUtils.isEmpty(addr)) {
                            mResult.setText(R.string.err);
                        } else {
                            mResult.setText(addr);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    mBtnSearch.setEnabled(true);

                }

                @Override
                public void onFailure(int i, String s, Throwable throwable) {
                    mResult.setText(R.string.err);
                    mBtnSearch.setEnabled(true);
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.getCurLoc)
    public void getCurLoc() {
        if (!client.isStarted()) {
            client.start();
        }
    }
}
