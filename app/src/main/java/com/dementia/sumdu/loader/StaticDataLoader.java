package com.dementia.sumdu.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;


import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.utils.DBManager;

import java.io.IOException;
import java.util.LinkedHashMap;

public class StaticDataLoader extends AsyncTaskLoader<LinkedHashMap<Integer, Pidrozdil>> {
    private DBManager mDbManager;
    public StaticDataLoader(Context context) {
        super(context);
        mDbManager = DBManager.getInstance(context);
    }

    @Override
    public LinkedHashMap<Integer, Pidrozdil> loadInBackground() {
        LinkedHashMap<Integer, Pidrozdil> galleryDataCursor;
        try {
            mDbManager.open();
            galleryDataCursor = mDbManager.getAllData();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }
        finally {
            mDbManager.close();
        }

        return galleryDataCursor;
    }
}
