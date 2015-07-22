package com.example.zaijie.gpslocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.lat)
    EditText mLat;
    @Bind(R.id.lng)
    EditText mLng;
    @Bind(R.id.btn_search)
    Button mBtnSearch;
    @Bind(R.id.result)
    TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_search)
    public void Search() {
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

                }

                @Override
                public void onFailure(int i, String s, Throwable throwable) {
                    mResult.setText(R.string.err);
                }
            });
        }
    }
}
