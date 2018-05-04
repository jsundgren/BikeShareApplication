package com.example.drakdraparen.bikeshareproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 *  ###Bike list fragment###
 *  This class handles the setup and interaction of the bike list fragment.
 *  The list is a recyclerview holding all the active bikes.
 **/

public class BikeListFragment extends Fragment implements Observer {

    private static BikesDB sharedBikes;
    private static final String LIST_TAG = "LIST_FRAGMENT";
    private BikeHoldAdapter mAdapter;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedBikes = BikesDB.get(getActivity());
        sharedBikes.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data){
        mAdapter.notifyDataSetChanged();
    }
    public class BikeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mAddressHeader, mBikeLocationTextView, mDistanceTextView, mDistanceHeader;
        public Bike mBike;
        public BikeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.bike_list_item, parent, false));
            mAddressHeader = itemView.findViewById(R.id.address);
            mBikeLocationTextView = itemView.findViewById(R.id.bike_location);
            mDistanceHeader = itemView.findViewById(R.id.distance_header);
            mDistanceTextView = itemView.findViewById(R.id.distance);
            itemView.setOnClickListener(this);
        }

        public void bind(Bike bike) {
            mBike = bike;
            mBikeLocationTextView.setText(((BikeMapActivity)getActivity()).getLocationAddress(mBike.getLocation()));
            mDistanceTextView.setText(Long.toString(mBike.getDistanceToUser()) + " m");
        }

        @Override
        public void onClick(View v){((BikeMapActivity)getActivity()).moveCamera(mBike.getLocation(), 13f);}
    }

    private class BikeHoldAdapter extends RecyclerView.Adapter<BikeHolder>  {
        private List<Bike> mBikes;

        public BikeHoldAdapter(List<Bike> bikes) {
            mBikes = bikes;
        }

        @Override
        public BikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BikeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BikeHolder holder, int pos) {
            Bike bike = mBikes.get(pos);
            holder.bind(bike);
            holder.itemView.setLongClickable(true);
        }

        @Override
        public int getItemCount() {
            return mBikes.size();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView bikesList = v.findViewById(R.id.list_recycler_view);
        bikesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BikeHoldAdapter(sharedBikes.getBikesDB());
        bikesList.setAdapter(mAdapter);
        return v;
    }
}
