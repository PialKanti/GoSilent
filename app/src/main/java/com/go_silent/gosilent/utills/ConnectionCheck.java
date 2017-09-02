package com.go_silent.gosilent.utills;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Pial on 9/2/2017.
 */

public class ConnectionCheck {
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsEnabled;
    }
}
