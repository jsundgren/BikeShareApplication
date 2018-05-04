package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  ###Bike info activity ###
 *  This class handles the setup of the bike info activity.
 *  In this view the user get showed information of the
 *  bike such as which type and the price renting it.
 *  It also shows a picture of the bike at from a specific
 *  ride (the ride the user clicks in the recycler view).
 **/

public class BikeInfoActivity extends AppCompatActivity{
    private ImageView mBikeImageView;
    private static BikesDB sBikesDB;
    private static final String BIKE_INFO_TAG = "START_RIDE_TAG";
    private FragmentManager fm = getSupportFragmentManager();
    String BikeID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_info);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        sBikesDB = BikesDB.get(this);
        BikesDB sBikesDB = BikesDB.get(this);
        Intent intent = getIntent();

        /* Get the ID of the bike user selected to compare in the database*/
        try {
            BikeID = intent.getExtras().getString("id");
        }catch(NullPointerException e){
            Log.d(BIKE_INFO_TAG, e.toString());
        }

        Bike bike = sBikesDB.getBike(BikeID);
        TextView bikeType = findViewById(R.id.bike_type);
        bikeType.setText(bike.getType());
        TextView pricePerHour = findViewById(R.id.bike_price);
        pricePerHour.setText(Integer.toString(bike.getPrice()) + "kr/h");
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BikeInfoActivity.super.finish();
            }
        });
        mBikeImageView = findViewById(R.id.ride_pic);
        /* Get the picture from the ride selected in the list */
        if(sBikesDB.getSpecificBikesDB(BikeID).size() > 0) {
            mBikeImageView.setImageBitmap(ConvertByteArrayToBitmap(sBikesDB.getSpecificBikesDB(BikeID).get(0).getMPictureOfBike()));
        }
        Bundle bundle = new Bundle();
        bundle.putString("id", BikeID);
        Fragment fragment = new RideListFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.ride_fragment_container,fragment).commit();
    }

    public static Bitmap ConvertByteArrayToBitmap(byte[] blop) {
        return BitmapFactory.decodeByteArray(blop, 0, blop.length);
    }

}
