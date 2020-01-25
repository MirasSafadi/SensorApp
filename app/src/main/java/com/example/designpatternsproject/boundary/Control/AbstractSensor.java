package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public abstract class AbstractSensor implements SensorEventListener {
    protected TextView resTV;
    protected Sensor sensor;
    protected final Context context;

    protected AbstractSensor(TextView resTV, Context context,Sensor sensor) throws SensorNotAvailableException{
        this.context = context;
        this.resTV = resTV;
        this.sensor = sensor;
    }
    public Sensor getSensor(){
        return sensor;
    }
}
