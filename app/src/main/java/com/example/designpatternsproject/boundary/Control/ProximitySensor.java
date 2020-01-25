package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

public class ProximitySensor extends AbstractSensor{
    private static ProximitySensor singleton = null;

    public static synchronized ProximitySensor getInstance(TextView resTV, SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new ProximitySensor(resTV,mSensorManager,context);
        return singleton;
    }

    private ProximitySensor(TextView resTV,SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        super(resTV,context,mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        if(sensor == null)
            throw new SensorNotAvailableException("Exception: Proximity Sensor not available",new Throwable());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentValue = event.values[0];
        if(currentValue <= 2.5)
            resTV.setText("Near");
        else
            resTV.setText("away");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
