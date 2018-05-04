package com.example.drakdraparen.bikeshareproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 *  ###ADDORFINDBIKE###
 *  This class handles the UI elements to either add a bike
 *  or find a bike (start BikeRegisterActivity/BikeListFragment).
 */

public class AddOrFindBikeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_addfind_bike, container, false);
        Button registerBikeButton = v.findViewById(R.id.register_bike);
        Button searchBikeButton = v.findViewById(R.id.search_bike);

        registerBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BikeRegisterActivity.class);
                startActivity(intent);
            }
        });

        searchBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BikeListFragment bikeListFragment = new BikeListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, bikeListFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }
}
