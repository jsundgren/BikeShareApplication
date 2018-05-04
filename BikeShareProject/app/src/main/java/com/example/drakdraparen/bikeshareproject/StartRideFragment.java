package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  ###Start ride fragment###
 *  This class handles the setup of the start ride fragment.
 **/

public class StartRideFragment extends Fragment {
    private Date startTime;
    private static final String START_RIDE_TAG = "START_RIDE_TAG";
    private static BikesDB sBikesDB;
    private static RidesDB sRidesDB;
    private static UserDB sUserDB;
    private User user;
    private Bike bike;
    String address, BikeID;

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
        /* Get the ID and the address of the bike */
        address = this.getArguments().getString("address");
        BikeID = this.getArguments().getString("id");
        bike = sBikesDB.getBike(BikeID);
        startPosition.setText(address);
        Button startRideButton = v.findViewById(R.id.start_ride_button);
        startRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // COMMENT OUT THE IF-STATEMENT BELOW IF THERE'S NO BIKES CLOSE BY
                if(bike.getDistanceToUser() < 200) {
                    if (bike.getPrice() > user.getMBalance()) {
                        Toast.makeText(getActivity(), "You need to refill your balance to book this bike", Toast.LENGTH_LONG).show();
                    } else {
                        if (address != null && user != null) {
                            bike.setActive();
                            sBikesDB.update(bike);
                            startTime = new Date();
                            Format formatter = new SimpleDateFormat("HH:mm");
                            String st = formatter.format(startTime.getTime());
                            sRidesDB.addRide(user.getMName(), address, st, BikeID);
                            EndRideFragment endridefrag = new EndRideFragment();
                            Bundle b = new Bundle();
                            b.putString("id", BikeID);
                            b.putString("st", st);
                            endridefrag.setArguments(b);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, endridefrag, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "You have to get closer to the bike to start a ride!", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton bikeInfo = v.findViewById(R.id.bike_infomation);
        bikeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BikeInfoActivity.class);
                intent.putExtra("id",BikeID);
                startActivity(intent);
            }
        });
        return v;
    }
}
