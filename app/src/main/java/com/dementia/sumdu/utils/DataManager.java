package com.dementia.sumdu.utils;

import com.dementia.sumdu.data.Pidrozdil;

import java.util.LinkedHashMap;

/**
 * Created by Vadim on 18.12.2016.
 */

public class DataManager {
    private static DataManager mInstance;
    private LinkedHashMap<Integer, Pidrozdil> alldata;
    public static DataManager getInstance() {
        if(mInstance == null) {
            mInstance = new DataManager();
        }
        return mInstance;
    }
    public void setAlldata(LinkedHashMap<Integer, Pidrozdil> alldata){
        this.alldata=alldata;
    }
    public LinkedHashMap<Integer, Pidrozdil> getlldata(){
       return alldata;
    }
}
