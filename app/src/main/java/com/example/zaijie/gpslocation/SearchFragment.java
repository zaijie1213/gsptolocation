package com.example.zaijie.gpslocation;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.zaijie.gpslocation.bean.History;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LocationClient client;


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        initLocation();
        return view;
    }

    private void initLocation() {
        client = new LocationClient(getActivity());
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


    @OnClick(R.id.btn_search)
    public void Search() {
        mBtnSearch.setEnabled(false);
        mResult.setText("`");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "查询中", "正在请求数据", false, false);
        dialog.show();

        final String lat = mLat.getText().toString();
        String lng = mLng.getText().toString();
        if ((!TextUtils.isEmpty(lat)) && (!TextUtils.isEmpty(lng))) {
            Parameters parameters = new Parameters();
            parameters.add("lat", lat);
            parameters.add("lng", lng);

            JuheData.executeWithAPI(getActivity(), 15, "http://apis.juhe.cn/geo/", JuheData.GET, parameters, new DataCallBack() {
                @Override
                public void onSuccess(int i, String s) {
                    try {
                        mBtnSearch.setEnabled(true);
                        dialog.dismiss();
                        JSONObject object = new JSONObject(s);
                        String addr = object.getJSONObject("result").getString("address");
                        if (TextUtils.isEmpty(addr)) {
                            mResult.setText(R.string.err);
                        } else {
                            mResult.setText(addr);
                            saveHistory(lat, lat, addr);
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

    private void saveHistory(String lat, String lont, String location) {
        final History bean = new History(lat, lont, location);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                bean.save();
            }
        });
    }

    @OnClick(R.id.getCurLoc)
    public void getCurLoc() {
        if (!client.isStarted()) {
            client.start();
        }
        client.requestLocation();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
