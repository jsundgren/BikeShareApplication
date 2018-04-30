package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
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

    public void setRealm(Realm realm){ BikesDB.sRealm = realm; }

    public synchronized static BikesDB get(Context context) {
        if(sBikesDB == null){ sBikesDB = new BikesDB(context);}
        return sBikesDB;
    }

    public OrderedRealmCollection<Bike> getBikesDB() {
        return sRealm.where(Bike.class).findAll().sort("mDistanceToUser");
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

    public void update(Bike bike){
        sRealm.beginTransaction();
        sRealm.insertOrUpdate(bike);
        sRealm.commitTransaction();
    }

    public synchronized void addBike(Bike b){
        final Bike fBike = b;
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(fBike);
                Log.d(BIKES_DB_TAG, "Added bike to Realm");
            }
        });
        this.setChanged();
        notifyObservers();
    }

    private BikesDB(Context context) {
        this.setChanged();
        notifyObservers();
    }
}
