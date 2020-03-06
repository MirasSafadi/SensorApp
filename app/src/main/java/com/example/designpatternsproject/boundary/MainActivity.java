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
    private SensorData sensorData;//the object responsible for fetching and updating the sensor data


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the sensor data object
        sensorData = new SensorData.Builder().setContext(this).build();
    }
    /*
    start/stop listening to sensors depending on app state.
     */
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
