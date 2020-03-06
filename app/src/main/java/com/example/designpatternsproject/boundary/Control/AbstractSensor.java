package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
/*
This abstract class is the super-class of all basic sensors (light, pressure, proximity),
it implements the SensorEvenListener interface but does not implement any function
because each subclass has its' own implementation.
 */
public abstract class AbstractSensor implements SensorEventListener {
    /*
    It contains all attributes the basic sensors share
     */
    protected TextView resTV;//the text view that displays the data
    protected Sensor sensor;//the sensor object (to be initialized in subclass)
    protected final Context context;//the context of the application (i.e. the MainActivity context).

    /*
    Constructor: it receives values for all aforementioned attributes and sets them.
     */
    protected AbstractSensor(TextView resTV, Context context,Sensor sensor){
        this.context = context;
        this.resTV = resTV;
        this.sensor = sensor;
    }
    /*
    A getter method for the sensor attribute.
     */
    public Sensor getSensor(){
        return sensor;
    }

}
