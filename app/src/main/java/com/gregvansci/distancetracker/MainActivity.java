package com.gregvansci.distancetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;

    // references to the UI elements

    EditText distanceTravelled;
    TextView units;
    Button toggleTracking;

    float distanceTravelledMeters;
    boolean tracking;

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> scheduledFuture;

    Location lastLocation, curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // give each UI variable a value
        distanceTravelled = findViewById(R.id.distanceTravelled);
        units = findViewById(R.id.units);
        toggleTracking = findViewById(R.id.toggleTracking);

        distanceTravelledMeters = 0;
        distanceTravelled.setText(String.valueOf(distanceTravelledMeters));

        toggleTracking.setText("Start Tracking");

        // request permissions for location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission granted");
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }

        toggleTracking.setOnClickListener(v -> {
            if (tracking) {
                toggleTracking.setText("Start Tracking");
                tracking = false;
                stopTracking();
            } else {
                if (startTracking()) {
                    toggleTracking.setText("Stop Tracking");
                    tracking = true;
                }
            }
        });
    }

    private boolean startTracking() {
        // start the executor service to run every second and find the distance between the current location and the last location
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(updateDistance, 0, 2000, TimeUnit.MILLISECONDS);
        return true;
    }

    private void stopTracking() {
        // stop tracking
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Toast.makeText(this, "This app requires permissions to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @SuppressLint("MissingPermission")
    Runnable updateDistance = () -> {
        // check for permissions
        Toast.makeText(this, "Updating distance", Toast.LENGTH_SHORT).show();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // get the current location
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        curLocation = location;
                        if (lastLocation != null) {
                            // calculate the distance between the last location and the current location
                            addDistance(lastLocation.distanceTo(curLocation));
                        }
                        lastLocation = curLocation;
                    } else {
                        Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        curLocation = location;
//                        // if the last location is null, set it to the current location
//                        if (lastLocation == null) {
//                            lastLocation = curLocation;
//                        }
//                        // find the distance between the current location and the last location
//                        //addDistance(11.11f);
//
//                        addDistance(curLocation.distanceTo(lastLocation));
//                        // set the last location to the current location
//                        lastLocation = curLocation;
//                        Toast.makeText(MainActivity.this, "Location updated: " + curLocation.getLatitude() + " " +curLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        } else {
            Toast.makeText(MainActivity.this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    };

    private void addDistance(float val) {
        distanceTravelledMeters += val;
        String distanceTravelledString;
        distanceTravelledString = String.valueOf(distanceTravelledMeters);
        distanceTravelled.setText(distanceTravelledString);
    }
}