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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<TextView> textViews = new ArrayList<>();
        /*
        when passing the text views to the sensorData object, pass them in this order:
        light -> proximity -> pressure -> compass -> position
        */
        textViews.add((TextView) findViewById(R.id.lightResTV));//light
        textViews.add((TextView) findViewById(R.id.proximityResTV));//proximity
        textViews.add((TextView) findViewById(R.id.pressureResTV));//pressure
        textViews.add((TextView) findViewById(R.id.compassResTV));//compass
        textViews.add((TextView) findViewById(R.id.positionResTV));//position
        //initialize the sensor data object
        sensorData = new SensorData(textViews,this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        for (AbstractSensor as: sensorData.getSensorList()){
            if(as != null)
                (sensorData.getSensorManager()).registerListener(as,as.getSensor(),SensorData.sensorDelay);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        for (AbstractSensor as: sensorData.getSensorList()){
            (sensorData.getSensorManager()).unregisterListener(as);
        }
    }
}
