package com.gregvansci.distancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // references to the UI elements

    EditText distanceTravelled;
    TextView units;
    Button startTracking;

    float distanceTravelledMeters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // give each UI variable a value
        distanceTravelled = findViewById(R.id.distanceTravelled);
        units = findViewById(R.id.units);
        startTracking = findViewById(R.id.startTracking);

        distanceTravelledMeters = 0;
        distanceTravelled.setText(String.valueOf(distanceTravelledMeters));

        startTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDistance(11.11f);
            }
        });
    }

    private void startTracking() {
        distanceTravelledMeters += 1;
    }

    private void stopTracking() {

    }

    private void addDistance(float val) {
        distanceTravelledMeters += val;
        String distanceTravelledString;
        if (distanceTravelledMeters > 1000) {
            distanceTravelledString = String.valueOf(distanceTravelledMeters / 1000);
            units.setText("kilometers");
        }
        else if (distanceTravelledMeters > 100) {
            distanceTravelledString = String.valueOf((int)distanceTravelledMeters);
        } else {
            distanceTravelledString = String.valueOf(distanceTravelledMeters);
        }
        distanceTravelled.setText(distanceTravelledString);
    }
}