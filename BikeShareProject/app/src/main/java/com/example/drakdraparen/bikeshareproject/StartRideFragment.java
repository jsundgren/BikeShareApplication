package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmChangeListener;

/**
 *  ###Start ride fragment###
 *  This class handles the setup of the start ride fragment.
 **/

public class StartRideFragment extends Fragment {
    private FragmentManager fm = getFragmentManager();
    private Date startTime;
    private static final String START_RIDE_TAG = "START_RIDE_TAG";
    private static BikesDB sBikesDB;
    private static RidesDB sRidesDB;
    private static UserDB sUserDB;
    private User user;
    private Bike bike;
    String address, id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sRidesDB = RidesDB.get(getActivity());
        sBikesDB = BikesDB.get(getActivity());
        sUserDB = UserDB.get(getActivity());
        user = sUserDB.findUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_start_ride, container, false);
        TextView startPosition = v.findViewById(R.id.start_location);
        address = this.getArguments().getString("address");
        id = this.getArguments().getString("id");
        bike = sBikesDB.getBike(id);
        startPosition.setText(address);
        Button startRideButton = v.findViewById(R.id.start_ride_button);
        startRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bike.getPrice() > user.getMBalance()){
                    Toast.makeText(getActivity(), "You need to refill your balance to book this bike", Toast.LENGTH_LONG).show();
                }else {
                    if (address != null && user != null) {
                        bike.setActive();
                        sBikesDB.update(bike);
                        startTime = new Date();
                        Format formatter = new SimpleDateFormat("HH:mm");
                        String st = formatter.format(startTime.getTime());
                        sRidesDB.addRide(user.getMName(), address, st, id);
                        EndRideFragment endridefrag = new EndRideFragment();
                        Bundle b = new Bundle();
                        b.putString("id", id);
                        endridefrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, endridefrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        ImageView bikeInfo = v.findViewById(R.id.bike_infomation);
        bikeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BikeInfo.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        return v;
    }
}
