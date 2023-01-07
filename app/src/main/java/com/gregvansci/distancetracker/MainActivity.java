package com.gregvansci.distancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // references to the UI elements

    EditText distanceTravelled;
    TextView units;
    Button toggleTracking;

    float distanceTravelledMeters;
    boolean tracking;

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> scheduledFuture;

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

        toggleTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracking) {
                    toggleTracking.setText("Start Tracking");
                    tracking = false;
                    stopTracking();
                } else {
                    toggleTracking.setText("Stop Tracking");
                    tracking = true;
                    startTracking();
                }
            }
        });
    }

    private void startTracking() {
        // start tracking
        // get the current location and save it to a variable

        // start the executor service to run every second and find the distance between the current location and the last location
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(updateDistance, 666, 666, TimeUnit.MILLISECONDS);
    }

    private void stopTracking() {
        // stop tracking
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        // find the distance between the current location and the last location
        addDistance(11.11f);
    }

    Runnable updateDistance = new Runnable() {
        @Override
        public void run() {
            addDistance(11.11f);
        }
    };

    private void addDistance(float val) {
        distanceTravelledMeters += val;
        String distanceTravelledString;
        distanceTravelledString = String.valueOf(distanceTravelledMeters);
        distanceTravelled.setText(distanceTravelledString);
    }
}