package com.example.drakdraparen.bikeshareproject;

import android.app.Application;

import io.realm.Realm;

/**
 *  ###Bikeshare application###
 *  This class handles the initial setup of the application.
 **/

public class BikeShareApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
