package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class BikeInfo extends AppCompatActivity{
    private TextView mBikeType, mPrice;
    private ImageView mBikeImageView;
    private static Bike mBike;
    private static RidesDB sRidesDB;
    private static BikesDB sBikesDB;
    private static final String BIKE_INFO_TAG = "START_RIDE_TAG";
    private FragmentManager fm = getSupportFragmentManager();
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_info);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        sRidesDB = RidesDB.get(this);
        sBikesDB = BikesDB.get(this);
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        mBike = sBikesDB.getBike(id);
        mBikeType = findViewById(R.id.bike_type);
        mBikeType.setText(mBike.getType());
        mPrice = findViewById(R.id.bike_price);
        mPrice.setText(Integer.toString(mBike.getPrice()) + "kr/h");
        mBikeImageView = findViewById(R.id.ride_pic);
        if(sRidesDB.getSpecificRidesDB(id).size() > 0) {
            mBikeImageView.setImageBitmap(ConvertByteArrayToBitmap(sRidesDB.getSpecificRidesDB(id).get(0).getMBikeImage()));
        }

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        Fragment fragment = new RideListFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.ride_fragment_container,fragment).commit();
    }

    public static Bitmap ConvertByteArrayToBitmap(byte[] blop) {
        return BitmapFactory.decodeByteArray(blop, 0, blop.length);
    }

    public void setImage(int pos){
        mBikeImageView.setImageBitmap(ConvertByteArrayToBitmap(sRidesDB.getSpecificRidesDB(id).get(pos).getMBikeImage()));
    }

}
