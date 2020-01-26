package com.example.designpatternsproject.boundary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Bundle;

import com.example.designpatternsproject.R;
import com.example.designpatternsproject.boundary.Control.AbstractSensor;
import com.example.designpatternsproject.boundary.Control.CompassSensor;
import com.example.designpatternsproject.boundary.Control.LightSensor;
import com.example.designpatternsproject.boundary.Control.LocationSensor;
import com.example.designpatternsproject.boundary.Control.PressureSensor;
import com.example.designpatternsproject.boundary.Control.ProximitySensor;
import com.example.designpatternsproject.boundary.Control.SensorData;
import com.example.designpatternsproject.boundary.Control.SensorNotAvailableException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SensorData sensorData;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //add log to the bottom of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Allow location use")
                .setMessage("Allow this app to use location services?")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //request permission from user
                        System.out.println("Access Granted!");
                    }
                }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //throw an exception
                        System.out.println("Access Denied!");
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        //initialize the sensor data object
        sensorData = new SensorData.Builder().setContext(this).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        (sensorData.getSensorManager()).registerListener(sensorData.getCompassSensor(), sensorData.getCompassSensor().getAccelorometerSensor(), SensorData.sensorDelay);
        (sensorData.getSensorManager()).registerListener(sensorData.getCompassSensor(), sensorData.getCompassSensor().getMagnetometerSensor(), SensorData.sensorDelay);
        for (AbstractSensor as : sensorData.getSensorList()) {
            if (as != null)
                (sensorData.getSensorManager()).registerListener(as, as.getSensor(), SensorData.sensorDelay);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        (sensorData.getSensorManager()).unregisterListener(sensorData.getCompassSensor());
        for (AbstractSensor as : sensorData.getSensorList()) {
            (sensorData.getSensorManager()).unregisterListener(as);
        }
    }
}
