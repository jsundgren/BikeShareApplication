package com.example.drakdraparen.bikeshareproject;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
/**
   ###RIDE CLASS###
   This class handles the specific rides made the user. A
   ride object holds a randomized ID as a primary key, a
   name of the user, start location (address), end location
   (address), start time (HH:MM), end time (HH:MM), and
   which bike the ride was made with.
 **/
public class Ride extends RealmObject {
    @PrimaryKey
    private String mID;
    @Required
    private String mRider;
    @Required
    private String mStartLocationRide;
    @Required
    private String mEndLocationRide;
    @Required
    private String mStartTime;
    @Required
    private String mEndTime;
    @Required
    private String mBikeID;
    @Required
    private byte[] mBikeImage;

    public Ride(String name, String startLocation, String endLocation, String bikeID){
        mID = UUID.randomUUID().toString();
        mRider = name;
        mStartLocationRide = startLocation;
        mEndLocationRide = endLocation;
        mBikeID = bikeID;
    }
    public Ride(){
        mRider = "";
        mStartLocationRide = "";
        mEndLocationRide = "";
    }
    public Ride(String startTime, String endTime){
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public Ride(byte [] pic){
        mBikeImage = pic;
    }

    public String getMRider() {return mRider;}
    public void setMRider(String mRider) {this.mRider = mRider;}
    public String getMstartRide() {return mStartLocationRide;}
    public void setMstartRide(String mstartRide) {
        this.mStartLocationRide = mstartRide;
    }
    public String getMendRide() {return mEndLocationRide;}
    public void setMendRide(String mendRide) { this.mEndLocationRide = mendRide;}
    public String getMstartTime(){return mStartTime;}
    public void setMstartTime(String mstartTime){this.mStartTime = mstartTime;}
    public String getMendTime(){return mEndTime;}
    public void setMendTime(String mendRide){this.mEndTime = mendRide;}
    public void setMBikeImage(byte [] mBikeImage){this.mBikeImage = mBikeImage;}
    public byte[] getMBikeImage(){return mBikeImage;}
    public void setMBikeID(String mBikeID){this.mBikeID = mBikeID;}
}
