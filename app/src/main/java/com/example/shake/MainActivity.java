package com.example.shake;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    TextView accelerometerValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerValues = findViewById(R.id.accelerometerValues);
        accelerometerValues.setVisibility(View.INVISIBLE);

        Switch valuesSwitch = findViewById(R.id.valuesSwitch);

        valuesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    accelerometerValues.setVisibility(View.VISIBLE);
                }
                else {
                    accelerometerValues.setVisibility(View.INVISIBLE);
                }
            }
        });


        Button rotateButton = findViewById(R.id.rotateButton);

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float totalAcceleration = Math.abs(x) + Math.abs(y) + Math.abs(z);

        if (totalAcceleration > 15) {
            Log.d("Shake", "Device shaken! X = " + x + " m/s², Y = " + y + " m/s², Z = " + z + " m/s²");
            Toast.makeText(this, "Device shaken!", Toast.LENGTH_SHORT).show();

        }
        if (accelerometerValues.getVisibility() == View.VISIBLE) {

            accelerometerValues.setText("X = " + x + " m/s²\n"
                    + "Y = " + y + " m/s²\n"
                    + "Z = " + z + " m/s²");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}