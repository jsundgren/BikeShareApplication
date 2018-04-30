package com.example.drakdraparen.bikeshareproject;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 *  ###BIKE CLASS###
 *  This class handles the specific bike objects. Each
 *  bike object holds and ID, type of bike, position (LAT/LNG),
 *  distance to the user and if it is active (not used at the moment).
 **/

public class Bike extends RealmObject {

    @PrimaryKey
    private String mID;
    @Required
    private String mType;
    private int mPrice;
    private double mLatitude;
    private double mLongitude;
    private long mDistanceToUser;
    private Boolean isActive;
    public Bike(LatLng location, String type, int price){
        mID = UUID.randomUUID().toString();
        mPrice = price;
        mLatitude = location.latitude;
        mLongitude = location.longitude;
        mType = type;
        isActive = true;
    }

    public Bike(){}

    public void setDistanceToUser(long distance){
        getRealm().beginTransaction();
        this.mDistanceToUser = distance;
        getRealm().commitTransaction();
    }
    public long getDistanceToUser(){return mDistanceToUser;}
    public LatLng getLocation(){return new LatLng(mLatitude,mLongitude);}
    public void setLatitude(double latitude){
        getRealm().beginTransaction();
        mLatitude = latitude;
        getRealm().commitTransaction();
    }
    public void setLongitude(double longitude){
        getRealm().beginTransaction();
        mLongitude = longitude;
        getRealm().commitTransaction();
    }
    public String getId(){return mID;}
    public String getType(){return mType;}
    public int getPrice(){return mPrice;}
    public void setActive(){
        getRealm().beginTransaction();
        isActive = !isActive;
        getRealm().commitTransaction();
    }

}
