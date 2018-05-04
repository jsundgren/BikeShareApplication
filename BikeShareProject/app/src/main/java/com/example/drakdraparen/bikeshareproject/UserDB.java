package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import java.util.Observable;
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

    public User findUser(){
        sRealm.beginTransaction();
        User user = sRealm.where(User.class).findFirstAsync();
        sRealm.commitTransaction();
        return user;
    }

    public void update(User user){
        sRealm.beginTransaction();
        sRealm.insertOrUpdate(user);
        sRealm.commitTransaction();
    }

    private UserDB(Context context) {
        this.setChanged();
        notifyObservers();
    }
}
