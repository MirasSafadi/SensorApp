package com.example.designpatternsproject.boundary.Control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.designpatternsproject.R;

import java.util.ArrayList;

public class SensorData implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private ArrayList<AbstractSensor> sensorList;
    private LocationSensor locationSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private CompassSensor compassSensor;
    private Context context;
    private Activity thisActivity;
    public static final int sensorDelay = SensorManager.SENSOR_DELAY_GAME;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SensorData(Builder builder) {
        //initialize all sensors
        context = builder.context;
        thisActivity = (Activity) context;
        TextView resTV = (TextView) thisActivity.findViewById(R.id.positionResTV);
        sensorList = new ArrayList<>();
        locationSensor = LocationSensor.getInstance(resTV, context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        initSensorList(context);
        //move initialization of compass sensor to here.
    }
    private void initSensorList(Context context){
        TextView resTV = (TextView) thisActivity.findViewById(R.id.errorTV);
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.lightResTV);
            sensorList.add(LightSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.proximityResTV);
            sensorList.add(ProximitySensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.pressureResTV);
            sensorList.add(PressureSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        //move initialization to constructor.
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.compassResTV);
            compassSensor = CompassSensor.getInstance(resTV,sensorManager,context);
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
    }
    public ArrayList<AbstractSensor> getSensorList(){
        return sensorList;
    }
    public SensorManager getSensorManager() {
        return sensorManager;
    }
    public CompassSensor getCompassSensor(){
        return compassSensor;
    }
    public static class Builder{
        private Context context;

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        public SensorData build(){
            return new SensorData(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_LOCATION) {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        locationManager
                                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    locationManager.removeUpdates(locationSensor);
                }
        }

    }
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(thisActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Request location updates:
                locationManager
                        .requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
            }
        }
    }
    public void startListeningToSensors(){
        sensorManager.registerListener(compassSensor, compassSensor.getAccelerometerSensor(), SensorData.sensorDelay);
        sensorManager.registerListener(compassSensor, compassSensor.getMagnetometerSensor(), SensorData.sensorDelay);
        sensorManager.registerListener(compassSensor, compassSensor.getCompassSensor(), SensorData.sensorDelay);
        for (AbstractSensor as : sensorList) {
            if (as != null)
                sensorManager.registerListener(as, as.getSensor(), sensorDelay);
        }
    }
    public void stopListeningToSensors(){
        locationManager.removeUpdates(locationSensor);
        sensorManager.unregisterListener(compassSensor);
        for (AbstractSensor as : sensorList) {
            sensorManager.unregisterListener(as);
        }
    }
}
