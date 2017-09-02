package com.go_silent.gosilent;

import android.Manifest;
import android.app.TimePickerDialog;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProfileChangerActivity extends AppCompatActivity implements
        LocationListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener {
    private Toolbar mToolbar;
    private EditText mFromTime, mToTime;
    private TimePickerDialog mTimePickerDialog;
    private Calendar mCalendar;
    private MapFragment mapFragment;
    private int PERMISSION_REQUEST_CODE = 100;
    private int LOCATION_SETTING_REQUEST_CODE = 101;
    private GoogleApiClient mApiClient;
    private LocationRequest locationRequest;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Circle mCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_changer);
        initialize();
    }

    public void initialize() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (mToolbar != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setTitle("New Profile Change");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mFromTime = (EditText) findViewById(R.id.eFromTime);
        mToTime = (EditText) findViewById(R.id.eToTime);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fMap);
        mFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimePicker(mFromTime);
            }
        });

        mToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimePicker(mToTime);
            }
        });
        mapFragment.getMapAsync(this);
    }

    public void launchTimePicker(final TextView mTextView) {
        mCalendar = Calendar.getInstance();
        int hoursOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        mTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                // Formatting Time
                Time time = new Time(hourOfDay, minutes, 0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                // Setting formatted time to TextView
                mTextView.setText(simpleDateFormat.format(time));
            }
        }, hoursOfDay, minutes, false);
        mTimePickerDialog.show();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // requesting permission
            requestLocationPermission();
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(3 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mApiClient,
                        builder.build()
                );
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //  Location settings are not satisfied. Show the user a dialog
                        try {
                            status.startResolutionForResult(ProfileChangerActivity.this, LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                }
            }
        });

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMarker != null) {
            mMarker.remove();
        }
        if (mCircle != null) {
            mCircle.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);
        mMarker = mGoogleMap.addMarker(markerOptions);
        mCircle = mGoogleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(600)
                .strokeWidth(3)
                .strokeColor(getResources().getColor(R.color.mapCircleStrokeColor))
                .fillColor(getResources().getColor(R.color.mapCircleFillColor)));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Map", "Google Api client Connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("Map", "GPS Enabled");
            } else {
                Log.d("Map", "GPS Not Enabled");
                Toast.makeText(this, "Please enable GPS to show your location in map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
