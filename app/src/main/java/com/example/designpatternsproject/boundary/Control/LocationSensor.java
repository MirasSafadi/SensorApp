package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.example.designpatternsproject.R;

public class LocationSensor implements LocationListener {
    private TextView resTV;
    private final Context context;
    private static LocationSensor singleton;


    public static synchronized LocationSensor getInstance(TextView resTV,Context context){
        if(singleton == null)
            singleton = new LocationSensor(resTV,context);
        return singleton;
    }
    private LocationSensor(TextView resTV,Context context){
        this.resTV = resTV;
        this.context = context;
    }


    @Override
    public void onLocationChanged(Location loc) {
        //(lat,long)
        resTV.setText(context.getResources()
                .getString(R.string.label_position,loc.getLatitude(),loc.getLongitude()));
    }

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
