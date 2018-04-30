package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
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
    private ImageButton findBikeButton;
    private TextView userName, userBalance;
    private static final String BSA_TAG = "BIKE_SHARE_ACTIVITY";
    private static User u;
    private Realm realm;
    private static RidesDB sRidesDB;
    private static BikesDB sBikesDB;
    private static UserDB sUserDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_share);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        if (SyncUser.currentUser() != null) {
            this.setUpUI(true);
        } else {
            attemptLogin();
        }
    }

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
            User user = sUserDB.findUser();
            userName = findViewById(R.id.user_name);
            userBalance = findViewById(R.id.user_balance);
            findBikeButton = findViewById(R.id.find_bike);
            userName.setText(user.getMName());
            userBalance.setText(String.valueOf(user.getMBalance()));
            findBikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BikeShareActivity.this, BikeMapActivity.class);
                    startActivity(intent);
                }
            });
    }

}


            /*        sBikesDB.addBike(new Bike(new LatLng(55.6870343,12.567829), "MTB", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.692823, 12.607819), "MTB", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.690934, 12.594850), "Racer", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.685805, 12.552192), "Racer", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.693111, 12.541549), "Tandem", 100));
        sBikesDB.addBike(new Bike(new LatLng(55.667284, 12.586909), "Tandem", 100));
        sBikesDB.addBike(new Bike(new LatLng(55.662101, 12.561739), "Cruiser", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.667650, 12.556245), "Cruiser", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.673790, 12.554248), "BMX", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.663177, 12.601741), "BMX", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.667650, 12.556245), "Cruiser", 50));
        sBikesDB.addBike(new Bike(new LatLng(55.631142, 12.579767), "Tandem", 100));
        sBikesDB.addBike(new Bike(new LatLng(55.639330, 12.648047), "Racer", 50));*/