package com.dementia.sumdu.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dementia.sumdu.R;
import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.loader.StaticDataLoader;
import com.dementia.sumdu.utils.DataManager;

import java.util.LinkedHashMap;

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LinkedHashMap<Integer, Pidrozdil>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getLoaderManager().initLoader(2029, null, this).forceLoad();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        },3000);
    }

    @Override
    public Loader<LinkedHashMap<Integer, Pidrozdil>> onCreateLoader(int i, Bundle bundle) {
        return new StaticDataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LinkedHashMap<Integer, Pidrozdil>> loader,
                               LinkedHashMap<Integer, Pidrozdil> integerPidrozdilLinkedHashMap) {
        DataManager.getInstance().setAlldata(integerPidrozdilLinkedHashMap);

    }
    @Override
    public void onLoaderReset(Loader<LinkedHashMap<Integer, Pidrozdil>> loader) {}
}
