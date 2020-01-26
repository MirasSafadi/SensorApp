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

import com.example.designpatternsproject.R;

import java.util.ArrayList;

public class SensorData {
    private ArrayList<AbstractSensor> sensorList;
    private LocationSensor locationSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private CompassSensor compassSensor;
    public static final int sensorDelay = SensorManager.SENSOR_DELAY_GAME;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SensorData(Builder builder){
        Context context = builder.context;
        TextView resTV =(TextView) ((Activity) context).findViewById(R.id.positionResTV);
        sensorList = new ArrayList<>();
        locationSensor = LocationSensor.getInstance(resTV,context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        initSensorList(context);
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
    }
    private void initSensorList(Context context){
        TextView resTV = (TextView) ((Activity) context).findViewById(R.id.errorTV);
        try {
            resTV = (TextView) ((Activity) context).findViewById(R.id.lightResTV);
            sensorList.add(LightSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        try {
            resTV = (TextView) ((Activity) context).findViewById(R.id.proximityResTV);
            sensorList.add(ProximitySensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        try {
            resTV = (TextView) ((Activity) context).findViewById(R.id.pressureResTV);
            sensorList.add(PressureSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV.setText(e.getMessage());
        }
        try {
            resTV = (TextView) ((Activity) context).findViewById(R.id.compassResTV);
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
}
