package com.gregvansci.distancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // references to the UI elements

    EditText distanceTravelled;
    TextView units;
    Button startTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // give each UI variable a value
        distanceTravelled = findViewById(R.id.distanceTravelled);
        units = findViewById(R.id.units);
        startTracking = findViewById(R.id.startTracking);

        distanceTravelled.setText("0");
    }
}