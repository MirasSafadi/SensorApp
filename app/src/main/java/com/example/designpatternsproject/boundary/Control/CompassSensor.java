package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.designpatternsproject.R;

public class CompassSensor implements SensorEventListener {
    private static CompassSensor singleton = null;
    //class attributes
    protected TextView resTV;
    protected final Context context;
    float[] mGeomagnetic;
    private float[] mGravity;
    private Double OldDegrees;
    private SensorManager mySensorManager;
    private Sensor compassSensor;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;




    public static synchronized CompassSensor getInstance(TextView resTV, SensorManager mSensorManager, Context context)throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new CompassSensor(resTV,mSensorManager,context);
        return singleton;
    }

    private CompassSensor(TextView resTV, SensorManager mSensorManager, Context context) throws SensorNotAvailableException{
        this.resTV = resTV;
        this.context = context;
        this.mySensorManager = mSensorManager;
        compassSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(accelerometerSensor == null || magnetometerSensor == null || compassSensor == null)
            throw new SensorNotAvailableException("Exception: Compass Sensor not available",new Throwable());
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Double DegreeToDisplay = 0.0;
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values;
                break;
            case Sensor.TYPE_ORIENTATION:
                if (mGravity != null && mGeomagnetic != null) {
                    float R[] = new float[9];
                    float I[] = new float[9];

                    SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                    // orientation contains azimuth, pitch and roll
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    float azimuth = orientation[0];
                    Double rotation = Math.toDegrees(azimuth);
                    if (rotation < 0.0f) {
                        rotation += 360.0f;
                    }
                    DegreeToDisplay=Math.round(rotation * 100.0) / 100.0;
                    OldDegrees=DegreeToDisplay;
                    Double SmoothFactorCompass=0.5; // to smooth up the compass sensitivity - still not fully working
                    if (OldDegrees > DegreeToDisplay) {
                        OldDegrees = (OldDegrees + SmoothFactorCompass * ((360 + DegreeToDisplay - OldDegrees) % 360) + 360) % 360;
                    }
                    else {
                        OldDegrees = (OldDegrees - SmoothFactorCompass * ((360 - DegreeToDisplay + OldDegrees) % 360) + 360) % 360;
                    }

                }
                String str = String.format("%.1f\u00B0",DegreeToDisplay);
                resTV.setText(context.getResources().getString(
                        R.string.label_compass, str));
                break;
            default:
                return;
        }

    }
    public Sensor getAccelerometerSensor(){
        return accelerometerSensor;
    }
    public Sensor getMagnetometerSensor(){
        return magnetometerSensor;
    }
    public Sensor getCompassSensor(){
        return compassSensor;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
