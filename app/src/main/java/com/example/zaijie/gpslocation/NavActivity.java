package com.example.zaijie.gpslocation;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.zaijie.gpslocation.bean.History;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener{


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.container)
    FrameLayout mContainer;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    android.support.v4.app.FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        UmengUpdateAgent.update(this);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFm=getSupportFragmentManager();
        openQuery("","");
        initView();

    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_query:
                        openQuery("","");
                        break;
                    case R.id.navigation_history:
                        openHistory();
                        break;
                    case R.id.navigation_about:
                        openAboutPage();
                        break;
                }


                return true;
            }
        });
    }

    private void openAboutPage() {

        setTitle(getResources().getString(R.string.navigation_about));
        mFm.beginTransaction().replace(R.id.container,AboutFragment.newInstance()).commit();
        mDrawerLayout.closeDrawers();
    }

    private void openHistory() {
        setTitle(getResources().getString(R.string.navigation_history));
        mFm.beginTransaction().replace(R.id.container,HistoryFragment.newInstance()).commit();
        mDrawerLayout.closeDrawers();
    }

    private void openQuery(String lat,String lont) {
        setTitle(getResources().getString(R.string.navigation_query));
        mFm.beginTransaction().replace(R.id.container,SearchFragment.newInstance(lat,lont)).commit();
        mDrawerLayout.closeDrawers();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void OnHistorySelect(History history) {
        openQuery(history.getLati(), history.getLont());
    }
}
