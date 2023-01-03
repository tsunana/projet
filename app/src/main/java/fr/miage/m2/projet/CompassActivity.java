package fr.miage.m2.projet;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class CompassActivity extends AppCompatActivity
        implements SensorEventListener {

    private static final String TAG = "Compass";
    private ImageView btnCapture;

    //Correct Declaration
    private Sensor gsensor;
    private Sensor msensor;
    private Intent i_displaySprite;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    private float azimuth = 0f;
    private float currentAzimuth = 0;
    private SensorManager sensorManager;

    // compass arrow to rotate
    public ImageView compass= null;

    public CompassActivity(Context context /*ImageView compass2*/) {
        // this.compass = compass2;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        i_displaySprite = new Intent(this,DisplaySpriteActivity.class);
    }

    public void start() {
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_GAME);


    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void adjustArrow() {

        if (compass == null) {
            Log.i(TAG, "arrow view is not set");
            return;
        }

        //Log.i(TAG, "will set rotation from " + currentAzimuth + " to " + azimuth);

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        compass.startAnimation(an);


        if(currentAzimuth==180){
            startActivity(i_displaySprite);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // code à exécuter lorsque l'orientation de la boussole change


        final float alpha = 0.97f;
        synchronized (this) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];

                //Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2];
            }

            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + 360) % 360;
                adjustArrow();
            }
            float azimuth = event.values[0];


//ne fonctionne pas.
            if(azimuth==180){
                Toast.makeText(this, "SUD", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // code à exécuter lorsque la précision de la boussole change


    }
}