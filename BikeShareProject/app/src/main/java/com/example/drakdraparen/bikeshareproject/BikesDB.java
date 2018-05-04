package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.lang.ref.WeakReference;
import java.util.Observable;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 *  ###BIKESDB CLASS###
 *  This class handles the functions for the database holding all the bikes.
 **/

public class BikesDB extends Observable{
    private static Realm sRealm;
    private static BikesDB sBikesDB;
    private static final String BIKES_DB_TAG = "BIKESDB";
    private RealmResults<Bike> results;
    private static WeakReference<BikeMapActivity> bma;
    public static void updateActivity(BikeMapActivity activity) {
        bma = new WeakReference<BikeMapActivity>(activity);
    }

    private Bike mBike = new Bike("",0);

    public void setRealm(Realm realm){ BikesDB.sRealm = realm; }

    public synchronized static BikesDB get(Context context) {
        if(sBikesDB == null){ sBikesDB = new BikesDB(context);}
        return sBikesDB;
    }

    public OrderedRealmCollection<Bike> getBikesDB() {
        return sRealm.where(Bike.class).sort("mDistanceToUser").findAll();
    }
    public OrderedRealmCollection<Bike> getActiveBikes(){
        return sRealm.where(Bike.class).equalTo("isActive", true).findAll();
    }
    public Bike getBike(String id){
        Bike bike = sRealm.where(Bike.class).equalTo("mID", id).findFirst();
        return bike;
    }

    public void setListener(){
        results = sRealm.where(Bike.class).equalTo("isActive", false).findAllAsync();
        if(results != null) {
            results.addChangeListener(new RealmChangeListener<RealmResults<Bike>>() {
                @Override
                public void onChange(RealmResults<Bike> bike) {
                    bma.get().update();
                }
            });
        }
    }

    public synchronized void setTypeAndPriceToBike(String type, int price){
        mBike.setType(type);
        mBike.setPrice(price);
    }

    public OrderedRealmCollection<Bike> getSpecificBikesDB(String id){
        return sRealm.where(Bike.class).equalTo("mID", id).findAll();
    }

    public synchronized void setPictureToBike(byte[] pic){
        mBike.setMPictureOfBike(pic);
    }
    public byte[] getPictureOfBike(){return mBike.getMPictureOfBike();}
    public synchronized void setLocation(LatLng position){
        mBike.setStartLongitude(position.longitude);
        mBike.setStartLatitude(position.latitude);

    }
    public synchronized void bikedAdded(){
        addFullBike(mBike);
        mBike = new Bike("", 0);
        this.setChanged();
        notifyObservers();
    }

    public synchronized void addFullBike(Bike b){
        final Bike fBike = b;
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(fBike);
                Log.d(BIKES_DB_TAG, "Added Bike to Realm");
            }
        });
        this.setChanged();
        notifyObservers();
    }

    public void update(Bike bike){
        sRealm.beginTransaction();
        sRealm.insertOrUpdate(bike);
        sRealm.commitTransaction();
    }

    private BikesDB(Context context) {
        this.setChanged();
        notifyObservers();
    }
}
