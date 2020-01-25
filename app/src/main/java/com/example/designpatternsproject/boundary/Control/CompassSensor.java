package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.designpatternsproject.R;

public class CompassSensor extends AbstractSensor {
    private static CompassSensor singleton = null;

    public static synchronized CompassSensor getInstance(TextView resTV, SensorManager mSensorManager, Context context)throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new CompassSensor(resTV,mSensorManager,context);
        return singleton;
    }

    private CompassSensor(TextView resTV, SensorManager mSensorManager, Context context) throws SensorNotAvailableException{
        super(resTV,context,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
        if (sensor == null)
            throw new SensorNotAvailableException("Exception: Compass Sensor not available", new Throwable());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentValue = event.values[0];
        float degree = Math.round(currentValue*(180/Math.PI));
        String str = String.format("%.1f\u00B0",degree);
        resTV.setText(context.getResources().getString(
                R.string.label_compass, str));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
