package com.example.drakdraparen.bikeshareproject;

import android.content.Context;
import android.util.Log;

import java.util.Observable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
/**
  *  ###USER CLASS###
  *  This class handles the specific user objects. A user object
  *  holds a individual ID, a name and a balance.
 **/
public class User extends RealmObject{
    @PrimaryKey
    private String mID;
    @Required
    private String mName;
    private int mBalance;
    public User(String name, int balance){
        mID = UUID.randomUUID().toString();
        mName = name;
        mBalance = balance;
    }
    public User(){}
    public String getMName(){return mName;}
    public void setMBalance(int mBalance){
        getRealm().beginTransaction();
        this.mBalance = mBalance;
        getRealm().commitTransaction();
    }
    public int getMBalance(){return mBalance;}
}
