package com.example.drakdraparen.bikeshareproject;


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
    @Required
    private byte [] mUserProfilePic;
    private int mBalance;

    public User(String name, int balance, byte[] userProfilePic){
        mID = UUID.randomUUID().toString();
        mName = name;
        mBalance = balance;
        mUserProfilePic = userProfilePic;
    }
    public User(){}
    public String getMName(){return mName;}
    public void setMBalance(int mBalance){
        getRealm().beginTransaction();
        this.mBalance = mBalance;
        getRealm().commitTransaction();
    }
    public int getMBalance(){return mBalance;}
    public byte [] getMUserProfilePic(){return mUserProfilePic;};
}
