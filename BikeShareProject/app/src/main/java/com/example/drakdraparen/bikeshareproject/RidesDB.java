package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import android.util.Log;
import java.util.Observable;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
/**
 *  ###RIDESDB CLASS###
 *  This class handles the functions for the database holding all the rides.
 **/

public class RidesDB extends Observable {
    private static Realm sRealm;
    private static RidesDB sRidesDB;
    private static final String RIDES_DB_TAG = "RIDESDB";

    private Ride mRide = new Ride("", "", "", "");

    public void setRealm(Realm realm){ RidesDB.sRealm = realm; }

    public synchronized static RidesDB get(Context context) {
        if (sRidesDB == null) {
            sRidesDB = new RidesDB(context);
        }
        return sRidesDB;
    }

    public OrderedRealmCollection<Ride> getRidesDB() {
        return sRealm.where(Ride.class).findAll();
    }

    public OrderedRealmCollection<Ride> getSpecificRidesDB(String id){
        return sRealm.where(Ride.class).equalTo("mBikeID", id).findAll();
    }

    /* Starting a new ride, adding the name and start point of the rider to an object of type Ride */
    public synchronized void addRide(String user, String startlocation, String startTime, String id) {
        mRide.setMRider(user);
        mRide.setMstartRide(startlocation);
        mRide.setMstartTime(startTime);
        mRide.setMBikeID(id);
    }

    /* Ending a ride, adding the endpoint to the current object and then add it to mallrides */
    public synchronized void endRide(String where, String endTime, int priceForRide) {
        mRide.setMendRide(where);
        mRide.setMendTime(endTime);
        mRide.setPriceForRide(priceForRide);
        addFullRide(mRide);
        mRide = new Ride("", "", "", "");
        this.setChanged();
        notifyObservers();
    }

    public synchronized void addFullRide(Ride r){
        final Ride fRide = r;
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(fRide);
                Log.d(RIDES_DB_TAG, "Added ride to Realm");
            }
        });
        this.setChanged();
        notifyObservers();
    }

    private RidesDB(Context context) {
        this.setChanged();
        notifyObservers();
    }
}