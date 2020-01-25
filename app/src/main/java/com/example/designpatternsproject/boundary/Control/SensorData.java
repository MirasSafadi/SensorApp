package com.example.designpatternsproject.boundary.Control;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;

public class SensorData {
    private ArrayList<AbstractSensor> sensorList;
    private LocationSensor locationSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    public static final int sensorDelay = SensorManager.SENSOR_DELAY_GAME;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SensorData(ArrayList<TextView> textViews, Context context){
        sensorList = new ArrayList<>();
        locationSensor = LocationSensor.getInstance(textViews.get(4),context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        initSensorList(textViews,context);
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
    }
    private void initSensorList(ArrayList<TextView> textViews, Context context){
        try {
            sensorList.add(LightSensor.getInstance(textViews.get(0),sensorManager,context));
        } catch (SensorNotAvailableException e) {
            textViews.get(0).setText(e.getMessage());
        }
        try {
            sensorList.add(ProximitySensor.getInstance(textViews.get(1),sensorManager,context));
        } catch (SensorNotAvailableException e) {
            textViews.get(1).setText(e.getMessage());
        }
        try {
            sensorList.add(PressureSensor.getInstance(textViews.get(2),sensorManager,context));
        } catch (SensorNotAvailableException e) {
            textViews.get(2).setText(e.getMessage());
        }
        try {
            sensorList.add(CompassSensor.getInstance(textViews.get(3),sensorManager,context));
        } catch (SensorNotAvailableException e) {
            textViews.get(3).setText(e.getMessage());
        }
    }
    public ArrayList<AbstractSensor> getSensorList(){
        return sensorList;
    }
    public SensorManager getSensorManager() {
        return sensorManager;
    }
}
