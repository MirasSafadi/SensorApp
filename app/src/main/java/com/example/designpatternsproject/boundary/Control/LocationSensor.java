package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.example.designpatternsproject.R;
/*
This class listens for change in current position and updates the display accordingly.
It implements the LocationListener interface.
It implements the Singleton design pattern since only one instance is needed.
 */
public class LocationSensor implements LocationListener {
    private TextView resTV;//the textView that displays the data
    private final Context context;//the context of the app
    private static LocationSensor singleton;//the single instance of the class

    /*
    this function is part of the singleton design pattern, it and the private constructor make sure
    that only one instance is created during the lifetime of the app.
    It throws a SensorNotAvailableException (see documentation) because the constructor does.
    */
    public static synchronized LocationSensor getInstance(TextView resTV,Context context){
        if(singleton == null)
            singleton = new LocationSensor(resTV,context);
        return singleton;
    }
    //the private constructor, initializes the attributes
    private LocationSensor(TextView resTV,Context context){
        this.resTV = resTV;
        this.context = context;
    }

    //if the location changes update the display
    @Override
    public void onLocationChanged(Location loc) {
        if(loc != null)
            resTV.setText(context.getResources()
                .getString(R.string.label_position,loc.getLatitude(),loc.getLongitude()));
    }
    //some extra method from the interface we don't need
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
