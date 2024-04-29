package com.example.caltrack

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.caltrack.data.InternalStorageHelper
import com.example.caltrack.data.TrackingLog
import com.example.caltrack.data.WaterLogItem
import java.time.LocalDateTime

class WaterLogActivity : AppCompatActivity() {
    lateinit var heroTxt: TextView
    lateinit var logTxt: TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_log)
        val bundle:Bundle? = intent.extras
        heroTxt = findViewById(R.id.heroTxt)
        logTxt = findViewById(R.id.water_log_txt)

        val name = getIntent().getExtras()?.getString("name")
        val waterConsumed = getIntent().getExtras()?.getFloat("consumed")
        val waterTotal = getIntent().getExtras()?.getFloat("total")

        heroTxt.text = """${waterConsumed?.toInt()} of ${waterTotal?.toInt()} L consumed"""

        val waterRating = findViewById<RatingBar>(R.id.water_rating)
        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            val totalStars = waterRating .numStars
            val rating = waterRating .rating
            val current = LocalDateTime.now()
            val hour = current.hour
            val minute = current.minute
            val volume = if (rating * 250 >= 1000) {
                "${rating * 0.25} L"
            } else {
                "${rating * 250} ml"
            }
            val logData = InternalStorageHelper(this).readFile(
                name + "_log.json",
                TrackingLog()
            ) as TrackingLog

            logData.water_logs.add(WaterLogItem((rating * 0.25).toFloat(), "$hour:$minute"))
            logData.waterConsumed += (rating * 0.25).toFloat()

            InternalStorageHelper(this).writeFile(
                name + "_log.json",
                logData
            )
            Toast.makeText(this,"logging ${rating} glasses - ${volume} at $hour:$minute",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        waterRating .setOnRatingBarChangeListener{ ratingBar, rating, fromUser ->
            val volume = if (rating * 250 >= 1000) {
                "${rating * 0.25} L"
            } else {
                "${rating * 250} ml"
            }
            logTxt.text = "logging ${rating} glasses - ${volume}"
        }
    }
}