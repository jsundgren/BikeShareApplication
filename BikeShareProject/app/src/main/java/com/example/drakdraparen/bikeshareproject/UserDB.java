package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

/**
 *  ###USERDB CLASS###
 *  This class handles the functions for the database holding all the users.
 **/

public class UserDB extends Observable {
    private static Realm sRealm;
    private static UserDB sUserDB;
    private static final String USER_DB_TAG = "USERDB";

    public void setRealm(Realm realm){ UserDB.sRealm = realm; }

    public synchronized static UserDB get(Context context) {
        if(sUserDB == null){ sUserDB = new UserDB(context);}
        return sUserDB;
    }

    public OrderedRealmCollection<User> getBikesDB() {
        return sRealm.where(User.class).findAll();
    }

    public User findUser(){
        User user = sRealm.where(User.class).findFirst();
        return user;
    }

    public void update(User user){
        sRealm.beginTransaction();
        sRealm.insertOrUpdate(user);
        sRealm.commitTransaction();
    }

    public synchronized void addUser(User u){
        final User fUser = u;
        sRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(fUser);
                Log.d(USER_DB_TAG, "Added bike to Realm");
            }
        });
        this.setChanged();
        notifyObservers();
    }

    private UserDB(Context context) {
        this.setChanged();
        notifyObservers();
    }
}
