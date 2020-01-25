package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.designpatternsproject.R;

public class PressureSensor extends AbstractSensor{
    private static PressureSensor singleton = null;

    public static synchronized PressureSensor getInstance(TextView resTV,SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new PressureSensor(resTV,mSensorManager,context);
        return singleton;
    }

    private PressureSensor(TextView resTV,SensorManager mSensorManager,Context context) throws SensorNotAvailableException{
        super(resTV,context,mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
        if(sensor == null)
            throw new SensorNotAvailableException("Exception: Pressure Sensor not available",new Throwable());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentValue = event.values[0];
        resTV.setText(context.getResources().getString(
                R.string.label, currentValue));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
