package com.example.drakdraparen.bikeshareproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 *  ###Map activity###
 *  This class handles the setup of the fragment holding the map. It also
 *  contains all the functions needed for the map information and interaction.
 *
 **/
public class BikeMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    Location mLastLocation;
    private LocationManager locationManager;
    private FragmentManager fm = getSupportFragmentManager();
    private static final String MAP_TAG = "Mapactivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static BikesDB sBikesDB;
    List<Bike> activeBikes;
    private boolean GPS_ACTIVE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_map);
        sBikesDB = BikesDB.get(this);
        BikesDB.updateActivity(this);
        BikeRegisterActivity.updateActivity(this);
        EndRideFragment.updateActivity(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocationPermission();
        createFragment(new AddOrFindBikeFragment(), R.id.fragment_container, null);
        sBikesDB.setListener();
    }

    /* Overrides the back-pressed function so the user cant use the back button while on the EndRideFragment */
    @Override
    public void onBackPressed(){
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if(f instanceof EndRideFragment){
            Toast.makeText(this, "Please press the end ride button if you want to end the ride!", Toast.LENGTH_LONG).show();
        }else{
            super.onBackPressed();
            startActivity(new Intent(BikeMapActivity.this, BikeShareActivity.class));
            finish();
        }
    }

    /* Init the map and checks permissions */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        drawMarkers();
    }

    /* Setup of the map fragment */
    public void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /* Draw the Google Maps markers at the position of all the active bikes */
    public void drawMarkers() {
        activeBikes = sBikesDB.getActiveBikes();
        for (int i = 0; i < activeBikes.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(activeBikes.get(i).getLocation()).title(activeBikes.get(i).getType()).snippet(activeBikes.get(i).getId()));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        String id = marker.getSnippet();
                        String a = getLocationAddress(marker.getPosition());
                        Bundle bundle = new Bundle();
                        bundle.putString("address", a);
                        bundle.putString("id", id);
                        createFragment(new StartRideFragment(), R.id.fragment_container, bundle);
                        return true;
                    }
            });
        }
    }

    /* Function to get the device location if the user accepts the runtime permission */
    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            Log.d(MAP_TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            mLastLocation = currentLocation;
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            calcDistance();
                        } else {
                            Log.d(MAP_TAG, "onComplete: Location is null");
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e(MAP_TAG,"getDevicelocation: " + e.getMessage());
        }
    }

    /* Moves the camera on the map (the view) */
    public void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
    }

    /* Function to get the address of a location using lat/lng */
    public String getLocationAddress(LatLng latlng){
        List<Address> listAddress;
        String address = "";
        double latitude = latlng.latitude;
        double longitude = latlng.longitude;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            listAddress = geocoder.getFromLocation(latitude, longitude, 1);
            address = listAddress.get(0).getAddressLine(0);
        }catch(IOException e){
            e.printStackTrace();
        }
        return address;
    }

    /* Get location permission from the user */
    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
            initMap();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /* Checks if the user accept the GPS permission */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] granResults){
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                    initMap();
                }else{
                    Toast.makeText(this, "This application need to use your location to find bikes close by", Toast.LENGTH_LONG).show();
                    BikeMapActivity.super.finish();
                }
            }
        }
    }

    /* Gets called if the location changes */
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        calcDistance();
    }

    /* If the realm listener gets called, the map is cleard and the markers are redrawn */
    public void update(){
        if(mMap != null) {
            mMap.clear();
            Fragment f = fm.findFragmentById(R.id.fragment_container);
            if((f instanceof StartRideFragment) || (f instanceof BikeListFragment)){
                drawMarkers();
            }
        }
    }
    /* Get the address for the end location */
    public String getEndLocation(){
        return getLocationAddress(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
    }
    /* Get the coordinates for the end position */
    public LatLng getEndCoord(){
        return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    /* If the GPS is turned off */
    public void onProviderEnabled(String provider) {GPS_ACTIVE = true;}
    /* If the GPS is turned off */
    public void onProviderDisabled(String provider) {GPS_ACTIVE = false;}
    /* Getter for the GPS boolean*/
    public boolean isGPSActive(){return GPS_ACTIVE;}
    /* Calculate distance between user and active bikes */
    public void calcDistance(){
        activeBikes = sBikesDB.getActiveBikes();
        for(int i = 0; i < activeBikes.size(); i++){
            Location BikeLocation = new Location("");
            BikeLocation.setLatitude(activeBikes.get(i).getLocation().latitude);
            BikeLocation.setLongitude(activeBikes.get(i).getLocation().longitude);
            activeBikes.get(i).setDistanceToUser((Math.round(mLastLocation.distanceTo(BikeLocation))));
            sBikesDB.update(activeBikes.get(i));
        }
    }
    /* Setup fragments */
    public void createFragment(Fragment frag, int res, Bundle b){
        Fragment fragment = fm.findFragmentById(res);
        if(b != null) {
            if (fragment == null) {
                fragment = frag;
                fragment.setArguments(b);
                fm.beginTransaction().add(res, fragment).commit();
            } else {
                frag.setArguments(b);
                fm.beginTransaction().remove(fragment).add(res, frag).commit();
            }
        }else{
            if (fragment == null) {
                fragment = frag;
                fm.beginTransaction().add(res, fragment).commit();
            } else {
                fm.beginTransaction().remove(fragment).add(res, frag).commit();
            }
        }
    }

}
