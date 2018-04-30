package com.example.drakdraparen.bikeshareproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import static com.example.drakdraparen.bikeshareproject.Constants.AUTH_URL;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *  ###End ride fragment###
 *  This class handles the setup of the end ride fragment.
 **/
public class EndRideFragment extends Fragment {
    private static final String END_RIDE_TAG = "END_RIDE_TAG";
    static final int REQUEST_PHOTO = 1;
    private Date endTime;
    private static RidesDB sRidesDB;
    private static BikesDB sBikesDB;
    private static UserDB sUserDB;
    private User user;
    private static Bike bike;
    URI uri;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sRidesDB = RidesDB.get(getActivity());
        sBikesDB = BikesDB.get(getActivity());
        sUserDB = UserDB.get(getActivity());
        user = sUserDB.findUser();
        id = this.getArguments().getString("id");
        bike = sBikesDB.getBike(id);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_end_ride, container, false);
        Button endRide = v.findViewById(R.id.end_ride);
        endRide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bike.setLatitude(((BikeMapActivity) getActivity()).getEndCoord().latitude);
                bike.setLongitude(((BikeMapActivity) getActivity()).getEndCoord().longitude);
                startCamera();
            }
        });
        return v;
    }

    public void startCamera(){
        Intent captureImage= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode != Activity.RESULT_OK) { return; }
        if (reqCode == REQUEST_PHOTO) {
            bike.setActive();
            sBikesDB.update(bike);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            endTime = new Date();
            Format formatter = new SimpleDateFormat("HH:mm");
            String et = formatter.format(endTime.getTime());
            String endLocation = ((BikeMapActivity)getActivity()).getEndLocation();
            sRidesDB.endRide(endLocation, et, convertBitmapToByteArray(imageBitmap));
            user.setMBalance(user.getMBalance()-bike.getPrice());
            sUserDB.update(user);
            Intent intent = new Intent(getActivity(), BikeShareActivity.class);
            startActivity(intent);
        }
    }
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer= new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer);
        return buffer.toByteArray();
    }

}
