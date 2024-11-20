package com.vyvyvi.caltrack

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.sqrt

class StepsActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelorometerSensor: Sensor? = null
    private lateinit var stepsTv: TextView

    private lateinit var trackBtn: Button
    private lateinit var resetBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelorometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        stepsTv = findViewById(R.id.stepsTv)
        trackBtn = findViewById(R.id.trackBtn)
        resetBtn = findViewById(R.id.resetBtn)


        trackBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Started tracking steps at location", Toast.LENGTH_SHORT).show()
            accelorometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }

        }

        resetBtn.setOnClickListener {
            stepsTv.text = "${0}"
            Toast.makeText(this, "Resetting steps to 0.", Toast.LENGTH_SHORT).show()
            sensorManager.unregisterListener(this)
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        accelorometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val vectorSum = sqrt((x * x + y * y + z * z) + 2)

            if (vectorSum > 25) {
                stepsTv.text = "${stepsTv.text.toString().toInt() + 1}"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}