package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *  ###End ride fragment###
 *  This class handles the setup of the end ride fragment.
 **/
public class EndRideFragment extends Fragment {
    private static final String END_RIDE_TAG = "END_RIDE_TAG";
    private static RidesDB sRidesDB;
    private static BikesDB sBikesDB;
    private static UserDB sUserDB;
    private User user;
    private static Bike bike;
    String id, st;
    private static WeakReference<BikeMapActivity> bma;
    public static void updateActivity(BikeMapActivity activity) {
        bma = new WeakReference<BikeMapActivity>(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(END_RIDE_TAG, "END RIDE FRAG SKAPAS");
        super.onCreate(savedInstanceState);
        sRidesDB = RidesDB.get(getActivity());
        sBikesDB = BikesDB.get(getActivity());
        sUserDB = UserDB.get(getActivity());
        user = sUserDB.findUser();
        /* Get the bike id and start time of the ride */
        try {
            id = this.getArguments().getString("id");
            st = this.getArguments().getString("st");
        }catch(NullPointerException e){
            Log.e(END_RIDE_TAG,"getArguments: " + e.getMessage());
        }
        bike = sBikesDB.getBike(id);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_end_ride, container, false);
        String h = "0:%s";
        Chronometer chronometer = v.findViewById(R.id.chronometer);
        chronometer.setFormat(h);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Button endRide = v.findViewById(R.id.end_ride);
        endRide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            if(bma.get().isGPSActive()) {
                bike.setLatitude(((BikeMapActivity) getActivity()).getEndCoord().latitude);
                bike.setLongitude(((BikeMapActivity) getActivity()).getEndCoord().longitude);
                bike.setActive();
                sBikesDB.update(bike);
                Date endTime = new Date();
                Format formatter = new SimpleDateFormat("HH:mm");
                String et = formatter.format(endTime.getTime());
                String endLocation = ((BikeMapActivity)getActivity()).getEndLocation();
                sRidesDB.endRide(endLocation, et, calculateRidePrice(st, et, bike.getPrice()));
                user.setMBalance(user.getMBalance()-calculateRidePrice(st, et, bike.getPrice()));
                sUserDB.update(user);
                Intent intent = new Intent(getActivity(), BikeShareActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(), "Please turn on location service to end the ride!", Toast.LENGTH_LONG).show();
            }
            }
        });
        bma.get().update();
        return v;
    }

    /* Calculates the price for the ride with the given st (start time) and et (end time) */
    private int calculateRidePrice(String st, String et, int price){
        int totalPriceForRide = price;
        String delim = ":";
        int stPos=st.indexOf(delim);
        int startTimeHour = Integer.parseInt(st.substring(0, stPos));
        int etPos=et.indexOf(delim);
        int endTimeHour = Integer.parseInt(st.substring(0, etPos));
        if(endTimeHour - startTimeHour > 0){
            totalPriceForRide = (endTimeHour - startTimeHour)*price;
        }
        return totalPriceForRide;
    }
}
