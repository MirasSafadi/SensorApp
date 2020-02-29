package com.example.designpatternsproject.boundary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.example.designpatternsproject.R;
import com.example.designpatternsproject.boundary.Control.AbstractSensor;
import com.example.designpatternsproject.boundary.Control.SensorData;

public class MainActivity extends AppCompatActivity {
    private SensorData sensorData;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //add log to the bottom of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the sensor data object
        sensorData = new SensorData.Builder().setContext(this).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorData.startListeningToSensors();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorData.stopListeningToSensors();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorData.checkLocationPermission();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorData.stopListeningToSensors();
    }
}
