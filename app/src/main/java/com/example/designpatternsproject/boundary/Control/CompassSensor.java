package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.designpatternsproject.R;

public class CompassSensor implements SensorEventListener {
    //fix the compass class...
    protected TextView resTV;
    protected final Context context;
    private float[] gravityData = new float[3];
    private float[] geomagneticData  = new float[3];
    private boolean hasGravityData = false;
    private boolean hasGeomagneticData = false;
    private double rotationInDegrees;
    private Sensor accelorometerSensor;
    private Sensor magnetometerSensor;
    private static CompassSensor singleton = null;

    public static synchronized CompassSensor getInstance(TextView resTV, SensorManager mSensorManager, Context context)throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new CompassSensor(resTV,mSensorManager,context);
        return singleton;
    }

    private CompassSensor(TextView resTV, SensorManager mSensorManager, Context context) throws SensorNotAvailableException{
        this.resTV = resTV;
        this.context = context;
        accelorometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(accelorometerSensor == null || magnetometerSensor == null)
            throw new SensorNotAvailableException("Exception: Compass Sensor not available",new Throwable());
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, gravityData, 0, 3);
                hasGravityData = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, geomagneticData, 0, 3);
                hasGeomagneticData = true;
                break;
            default:
                return;
        }

        if (hasGravityData && hasGeomagneticData) {
            float identityMatrix[] = new float[9];
            float rotationMatrix[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, identityMatrix,
                    gravityData, geomagneticData);

            if (success) {
                float orientationMatrix[] = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientationMatrix);
                float rotationInRadians = orientationMatrix[0];
                rotationInDegrees = Math.toDegrees(rotationInRadians);
                // do something with the rotation in degrees
                String str = String.format("%.1f\u00B0",rotationInDegrees);
                resTV.setText(context.getResources().getString(
                        R.string.label_compass, str));
            }
        }

    }
    public Sensor getAccelorometerSensor(){
        return accelorometerSensor;
    }
    public Sensor getMagnetometerSensor(){
        return magnetometerSensor;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
