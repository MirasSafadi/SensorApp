package com.example.SensorApp.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;
import com.example.SensorApp.R;

/*This is a subclass of the AbstractSensor class, it is a basic sensor,
It implements the Singleton design pattern since only one instance is needed
 */
public class LightSensor extends AbstractSensor{
    private static LightSensor singleton = null;//the single instance
    /*
    this function is part of the singleton design pattern, it and the private constructor make sure
    that only one instance is created during the lifetime of the app.
    It throws a SensorNotAvailableException (see documentation) because the constructor does.
    */
    public static synchronized LightSensor getInstance(TextView resTV,SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new LightSensor(resTV,mSensorManager,context);
        return singleton;
    }
    /*
    the private constructor calls the super constructor and initializes the super attributes
    it receives a sensorManager object in order to initialize the sensor super attribute.
     */
    private LightSensor(TextView resTV,SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        super(resTV,context,mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        if(sensor == null)//if initialization of the sensor fails, throw a SensorNotAvailableException.
            throw new SensorNotAvailableException("Exception: Light Sensor not available",new Throwable());
    }
    /*
    when the sensor reads new data update the display accordingly
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentValue = event.values[0];
        resTV.setText(context.getResources().getString(
                R.string.label, currentValue));
    }
    //a method we don't need from the interface
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
