package com.go_silent.gosilent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FloatingActionButton mSilentProfile;
    private FloatingActionButton mLocationEvent;
    private int PERMISSION_REQUEST_CODE = 100;
    private int PLACE_PICKER_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        initialize();

    }

    /**
     * This initialize and all variables needed for the view
     */
    public void initialize() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mSilentProfile = (FloatingActionButton) findViewById(R.id.FAB_silent);
        mLocationEvent = (FloatingActionButton) findViewById(R.id.FAB_event);
        mSilentProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileChangerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void requestPermission() {
        // Permissions to be requested
        String[] permissionList = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        ArrayList<String> requestPermissionList = new ArrayList<>();
        // add permissions to arraylist which has not been granted
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }
        String[] requestList = requestPermissionList.toArray(new String[requestPermissionList.size()]);
        if (requestList.length > 0)
            ActivityCompat.requestPermissions(this, requestList, PERMISSION_REQUEST_CODE);
    }
}
