package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import static com.example.drakdraparen.bikeshareproject.Constants.AUTH_URL;
import static com.example.drakdraparen.bikeshareproject.Constants.USERNAME;
import static com.example.drakdraparen.bikeshareproject.Constants.REALM_BASE_URL;
import static com.example.drakdraparen.bikeshareproject.Constants.DB_FILE;
/**
 *  ###Bike share activity###
 *  This class handles the setup start screen.
 **/
public class BikeShareActivity extends AppCompatActivity{
    private static final String BSA_TAG = "BIKE_SHARE_ACTIVITY";
    private FragmentManager fm = getSupportFragmentManager();
    private static User user;
    private static UserDB sUserDB;
    private static BikesDB sBikesDB;
    private static RidesDB sRidesDB;
    private static Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        /* Check if there is a current user on this instance  */
        if (SyncUser.currentUser() != null) {
            this.setUpUI(true);
        } else {
            attemptLogin();
        }

    }

    /* Override the back button so the user can't go back to the EndRideFragment if there's just been a ended ride */
    @Override
    public void onBackPressed(){
        // Do nothing!
    }

    /* Login to the database / connect */
    private void attemptLogin() {
        SyncCredentials credentials= SyncCredentials.nickname(USERNAME, true);
        SyncUser.loginAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(SyncUser user) {
                Log.d(BSA_TAG, "LOGGED IN");
                setUpUI(true);
            }
            @Override
            public void onError(ObjectServerError error) {
                Log.d(BSA_TAG, "NOT LOGGED IN");
                setUpUI(false);
            }
        });
    }

    /* Only setup the UI if the device is logged in */
    private void setUpUI(boolean loginSuccess) {
        if (loginSuccess) {
            SyncConfiguration ride_configuration = new SyncConfiguration.Builder(
                    SyncUser.currentUser(), REALM_BASE_URL + DB_FILE).build();
            realm = Realm.getInstance(ride_configuration);
        } else {
            realm = Realm.getDefaultInstance();
        }
        sRidesDB = RidesDB.get(this);
        sRidesDB.setRealm(realm);
        sBikesDB = BikesDB.get(this);
        sBikesDB.setRealm(realm);
        sUserDB = UserDB.get(this);
        sUserDB.setRealm(realm);
        setUpSearchButton();
        createUIFragment();
    }

    /* Set up the search button */
    private void setUpSearchButton(){
        ImageButton findBikeButton = findViewById(R.id.find_bike);
        findBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BikeShareActivity.this, BikeMapActivity.class);
                startActivity(intent);
            }
        });
    }

    /* Set up the fragment holding the User information retrived from the Realm database */
    private void createUIFragment(){
        Fragment fragment = new StartUIFragment();
        fm.beginTransaction().add(R.id.UI_fragment_container,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}