package com.example.drakdraparen.bikeshareproject;

/**
 *  ###Constants###
 *  This class holds the constants used for the db connection.
 **/

final class Constants {
        static final String INSTANCE_ADDRESS = "bikeshareapplication.us1.cloud.realm.io";
        static final String AUTH_URL = "https://" + INSTANCE_ADDRESS + "/auth";
        static final String REALM_BASE_URL = "realms://" + INSTANCE_ADDRESS ;
        static final String USERNAME = "Provider";
        static final String DB_FILE = "/Ride";
}
