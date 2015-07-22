package com.example.zaijie.gpslocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

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


}
