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
    @Required
    private byte [] mPictureOfBike;
    private int mPrice;
    private double mLatitude;
    private double mLongitude;
    private long mDistanceToUser;
    private Boolean isActive;

    public Bike(String type, int price){
        mID = UUID.randomUUID().toString();
        mPrice = price;
        mType = type;
        isActive = true;
    }

    public Bike(){
        mType = "";
    }

    public Bike(LatLng location){
        mLatitude = location.latitude;
        mLongitude = location.longitude;
    }

    public Bike(byte [] pic){
        mPictureOfBike = pic;
    }

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
    public void setStartLatitude(double latitude){
        mLatitude = latitude;
    }
    public void setStartLongitude(double longitude){
        mLongitude = longitude;
    }
    public String getId(){return mID;}
    public String getType(){return mType;}
    public void setType(String type){this.mType = type;}
    public int getPrice(){return mPrice;}
    public void setPrice(int price){this.mPrice = price;}
    public void setActive(){
        getRealm().beginTransaction();
        isActive = !isActive;
        getRealm().commitTransaction();
    }
    public void setMPictureOfBike(byte [] mBikeImage){this.mPictureOfBike = mBikeImage;}
    public byte[] getMPictureOfBike(){return mPictureOfBike;}

}
