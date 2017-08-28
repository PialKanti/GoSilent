package com.go_silent.gosilent.database;

import android.provider.BaseColumns;

/**
 * Created by Pial on 8/25/2017.
 */

public final class DatabaseContract {
    private DatabaseContract(){

    }

    public static class PlaceEntry implements BaseColumns{
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LONG = "long";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_GEOFENCE_ID = "geofence_id";

    }
}
