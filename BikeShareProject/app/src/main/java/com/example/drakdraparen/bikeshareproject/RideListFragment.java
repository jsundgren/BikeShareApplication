package com.example.drakdraparen.bikeshareproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 *  ###Ride list fragment###
 *  This class handles the setup and interaction of the recyclerview
 *  holding the rides of each specific user showed when the user clicks
 *  on the information button connected to each bikes. (Showed
 *  in the start ride fragment.)
 **/

public class RideListFragment extends Fragment implements Observer {

    private static RidesDB sRidesDB;
    private static final String RIDE_LIST_TAG = "RIDE_LIST_FRAGMENT";
    private RecyclerView mRidesList;
    private RideHoldAdapter mAdapter;
    String id;
    int clickPos = 0;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sRidesDB = RidesDB.get(getActivity());
        sRidesDB.addObserver(this);
        id = this.getArguments().getString("id");
    }

    @Override
    public void update(Observable observable, Object data){
        mAdapter.notifyDataSetChanged();
    }

    public void updateList(){
        mAdapter.notifyDataSetChanged();
    }

    public class RideHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView mRecyclerView;
        private TextView mRiderTextView, mStartTextView, mEndTextView, mStartTimeTextView, mEndTimeTextView;
        public Ride mRide;
        public RideHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.ride_list_item, parent, false));
            mRiderTextView = itemView.findViewById(R.id.rider);
            mStartTextView = itemView.findViewById(R.id.start_point);
            mEndTextView = itemView.findViewById(R.id.end_point);
            mStartTimeTextView = itemView.findViewById(R.id.startTime);
            mEndTimeTextView = itemView.findViewById(R.id.endTime);
            itemView.setOnClickListener(this);
            mRecyclerView = new RecyclerView(getContext());
        }

        public void bind(Ride ride) {
            mRide = ride;
            mRiderTextView.setText(ride.getMRider());
            mStartTextView.setText(ride.getMstartRide());
            mEndTextView.setText(ride.getMendRide());
            mStartTimeTextView.setText(ride.getMstartTime());
            mEndTimeTextView.setText(ride.getMendTime());
        }

        @Override
        public void onClick(View v){
            int itemPosition = mRecyclerView.getChildLayoutPosition(v);
            ((BikeInfo)getActivity()).setImage(itemPosition);
        }
    }

    private class RideHoldAdapter extends RecyclerView.Adapter<RideHolder>  {
        private List<Ride> mRides;

        public RideHoldAdapter(List<Ride> Rides) {
            mRides = Rides;
        }

        @Override
        public RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RideHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RideHolder holder, int pos) {
            clickPos = pos;
            Ride ride = mRides.get(pos);
            holder.bind(ride);
            holder.itemView.setLongClickable(true);
        }

        @Override
        public int getItemCount() {
            return mRides.size();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mRidesList = v.findViewById(R.id.list_recycler_view);
        mRidesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RideHoldAdapter(sRidesDB.getSpecificRidesDB(id));
        mRidesList.setAdapter(mAdapter);
        return v;
    }
}
