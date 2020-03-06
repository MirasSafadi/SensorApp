package com.example.designpatternsproject.boundary.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.designpatternsproject.R;
/*
This class is one of the two non-basic sensors because it needs 3 sensor objects to get the data,
it implements the ServerEventListener interface and acts differently when each sensor is changed.
It also implements the Singleton design pattern, since only one instance of the class is needed for
the lifetime of the application.
 */
public class CompassSensor implements SensorEventListener {
    /*
    It needs more attributes than the basic sensors
     */
    private static CompassSensor singleton = null;//the single instance
    private TextView resTV;//the TextView that displays the data
    private final Context context;//the context of the application.
    private float[] mGeomagnetic;//a float array that is updated as the accelerometer sensor is updated
    private float[] mGravity;//a float array that is updated as the magnetometer sensor is updated
    private double OldDegrees;//a variable to save the old degrees to north.
    private SensorManager mySensorManager;//a SensorManager object that manages the 3 sensors
    private Sensor compassSensor;//the orientation sensor
    private Sensor accelerometerSensor;//the accelerometer sensor
    private Sensor magnetometerSensor;//the magnetometer sensor
    /*
    this function is part of the singleton design pattern, it and the private constructor make sure
    that only one instance is created during the lifetime of the app.
    It throws a SensorNotAvailableException (see documentation) because the constructor does.
     */
    public static synchronized CompassSensor getInstance(TextView resTV, SensorManager mSensorManager, Context context)throws SensorNotAvailableException{
        if(singleton == null)
            singleton = new CompassSensor(resTV,mSensorManager,context);
        return singleton;
    }
    /*
    The private constructor. It initializes all attributes.
    It throws SensorNotAvailableException if any one of the sensors fails to initalize for some reason.
     */
    private CompassSensor(TextView resTV, SensorManager mSensorManager, Context context) throws SensorNotAvailableException{
        this.resTV = resTV;
        this.context = context;
        this.mySensorManager = mSensorManager;
        //initialize the 3 sensors
        compassSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(accelerometerSensor == null || magnetometerSensor == null || compassSensor == null)
            throw new SensorNotAvailableException("Exception: Compass Sensor not available",new Throwable());
    }

    /*
    This function is called whenever any sensor reads new data.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        double degreeToDisplay = 0.0;
        OldDegrees = 0.0;
        //know which sensor read new data based on event.sensor.getType()
        switch (event.sensor.getType()){
            //if the accelerometer or the magnetometer read new data update the relevant variables.
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values;
                break;
            case Sensor.TYPE_ORIENTATION://if the compass sensor reads new data
                //make sure the accelerometer and the magnetometer read some data first
                if (mGravity != null && mGeomagnetic != null) {
                    float R[] = new float[9];
                    float I[] = new float[9];
                    //get the rotation matrix
                    SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                    // orientation contains azimuth, pitch and roll
                    float orientation[] = new float[3];
                    //get the orientation
                    SensorManager.getOrientation(R, orientation);
                    //get the azimuth and rotation
                    float azimuth = orientation[0];
                    double rotation = Math.toDegrees(azimuth);
                    //rotation is in degrees, if it's negative convert it to positive by adding 360 degrees.
                    if (rotation < 0.0f) {
                        rotation += 360.0f;
                    }
                    //compute the degrees to display
                    degreeToDisplay=Math.round(rotation * 100.0) / 100.0;
                    OldDegrees=degreeToDisplay;
                    double SmoothFactorCompass=0.9; // to smooth up the compass sensitivity
                    //smooth up the degree
                    if (OldDegrees > degreeToDisplay)
                        OldDegrees = (OldDegrees + SmoothFactorCompass * ((360 + degreeToDisplay - OldDegrees) % 360) + 360) % 360;
                    else
                        OldDegrees = (OldDegrees - SmoothFactorCompass * ((360 - degreeToDisplay + OldDegrees) % 360) + 360) % 360;

                }
                //if data was read from other sensors, display it according to above computations
                //if not display 0
                String str = String.format("%.1f\u00B0",OldDegrees);
                //String str = String.format("%.1f\u00B0",degreeToDisplay);
                resTV.setText(context.getResources().getString(
                        R.string.label_compass, str));
                break;
            default:
                return;
        }

    }
    /*
    getters and a function we don't need to implement in the interface
     */
    public Sensor getAccelerometerSensor(){
        return accelerometerSensor;
    }
    public Sensor getMagnetometerSensor(){
        return magnetometerSensor;
    }
    public Sensor getCompassSensor(){ return compassSensor; }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
