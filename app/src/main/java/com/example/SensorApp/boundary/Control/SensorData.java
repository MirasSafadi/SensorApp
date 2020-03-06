package com.example.SensorApp.boundary.Control;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.SensorApp.R;
import java.util.ArrayList;
/*
This class is an implementation to the Facade design pattern in order to hide the hideous procedure of
initializing all sensors, requesting permissions to use location services.
It also implements the Builder Design pattern.
 */
public class SensorData implements ActivityCompat.OnRequestPermissionsResultCallback {
    /*
    class attributes include a sensorManager, a locationManger, an array of basic sensors (AbstractSensors),
    and a compass and location sensors.
     */
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;//a const for location permissions
    private ArrayList<AbstractSensor> sensorList;
    private LocationSensor locationSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private CompassSensor compassSensor;
    private Context context;//the context of the app
    private Activity thisActivity;//the current activity, extracted from context
    public static final int sensorDelay = SensorManager.SENSOR_DELAY_GAME;//a const for registering sensor listeners
    /*
    The constructor is private as part of the Builder design pattern.
    It initializes all class attributes.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private SensorData(Builder builder) {
        //initialize all sensors
        context = builder.context;//get the context from the builder
        thisActivity = (Activity) context;//extract the Activity from the context
        sensorList = new ArrayList<>();
        //initialize the managers
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //call the method that initializes all sensors
        initSensorList();
    }
    /*
    This method initializes all sensors, if a SensorNotAvailableException is thrown catch it and
    update the corresponding textView.
     */
    private void initSensorList(){
        TextView resTV;
        resTV = (TextView) thisActivity.findViewById(R.id.positionResTV);
        locationSensor = LocationSensor.getInstance(resTV, context);
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.lightResTV);
            sensorList.add(LightSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV = (TextView) thisActivity.findViewById(R.id.lightResTV);
            resTV.setTextColor(Color.RED);
            resTV.setTypeface(resTV.getTypeface(), Typeface.BOLD_ITALIC);
            resTV.setText("Disabled");
        }
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.proximityResTV);
            sensorList.add(ProximitySensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV = (TextView) thisActivity.findViewById(R.id.proximityResTV);
            resTV.setTextColor(Color.RED);
            resTV.setTypeface(resTV.getTypeface(), Typeface.BOLD_ITALIC);
            resTV.setText("Disabled");
        }
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.pressureResTV);
            sensorList.add(PressureSensor.getInstance(resTV,sensorManager,context));
        } catch (SensorNotAvailableException e) {
            resTV = (TextView) thisActivity.findViewById(R.id.pressureResTV);
            resTV.setTextColor(Color.RED);
            resTV.setTypeface(resTV.getTypeface(), Typeface.BOLD_ITALIC);
            resTV.setText("Disabled");
        }
        //move initialization to constructor.
        try {
            resTV = (TextView) thisActivity.findViewById(R.id.compassResTV);
            compassSensor = CompassSensor.getInstance(resTV,sensorManager,context);
        } catch (SensorNotAvailableException e) {
            resTV = (TextView) thisActivity.findViewById(R.id.compassResTV);
            resTV.setTextColor(Color.RED);
            resTV.setTypeface(resTV.getTypeface(), Typeface.BOLD_ITALIC);
            resTV.setText("Disabled");
        }
    }
    /*
    A static inner class as part of the implementation of the Builder design pattern
     */
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
    /*
    This method is called when the user responds to permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        //use this const to know which permission was given (location, camera, etc_
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_LOCATION) {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates from location manager:
                        locationManager
                                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    locationManager.removeUpdates(locationSensor);
                    //display Disabled on position textView
                    TextView resTV = (TextView) thisActivity.findViewById(R.id.positionResTV);
                    resTV.setTextColor(Color.RED);
                    resTV.setTypeface(resTV.getTypeface(), Typeface.BOLD_ITALIC);
                    resTV.setText("Disabled");
                }
        }

    }
    /*
    This method checks if location permission are already granted or not.
     */
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //permission not granted
            //request it.
            ActivityCompat
                    .requestPermissions(thisActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        } else {
            //if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                //Request location updates from location manager:
                locationManager
                        .requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationSensor);
        }
    }
    /*
    this method registers all sensors to the sensor manager so the listening can begin
     */
    public void startListeningToSensors(){
        sensorManager.registerListener(compassSensor, compassSensor.getAccelerometerSensor(), sensorDelay);
        sensorManager.registerListener(compassSensor, compassSensor.getMagnetometerSensor(), sensorDelay);
        sensorManager.registerListener(compassSensor, compassSensor.getCompassSensor(), sensorDelay);
        for (AbstractSensor as : sensorList) {
            if (as != null)
                sensorManager.registerListener(as, as.getSensor(), sensorDelay);
        }
    }
    /*
    unregister all sensors
     */
    public void stopListeningToSensors(){
        locationManager.removeUpdates(locationSensor);
        sensorManager.unregisterListener(compassSensor);
        for (AbstractSensor as : sensorList) {
            sensorManager.unregisterListener(as);
        }
    }
}
