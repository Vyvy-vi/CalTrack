package com.vyvyvi.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vyvyvi.caltrack.data.FoodLogItem
import com.vyvyvi.caltrack.data.InternalStorageHelper
import com.vyvyvi.caltrack.data.TrackingLog
import com.vyvyvi.caltrack.data.WaterLogItem
import com.vyvyvi.caltrack.utils.Utils.convertEmailToKey
import java.time.LocalDateTime

class WaterLogActivity : AppCompatActivity() {
    lateinit var heroTxt: TextView
    lateinit var logTxt: TextView
    lateinit var auth: FirebaseAuth

    lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_log)

        heroTxt = findViewById(R.id.heroTxt)
        logTxt = findViewById(R.id.water_log_txt)

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        val waterConsumed = getIntent().getExtras()?.getFloat("consumed")
        val waterTotal = getIntent().getExtras()?.getFloat("total")

        heroTxt.text =
            """${"%.2f".format(waterConsumed)} of ${"%.2f".format(waterTotal)} L consumed"""

        val waterRating = findViewById<RatingBar>(R.id.water_rating)
        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            val rating = waterRating.rating
            val current = LocalDateTime.now()
            val hour = current.hour
            val minute = current.minute
            val volume = if (rating * 250 >= 1000) {
                "${rating * 0.25} L"
            } else {
                "${rating * 250} ml"
            }

            val waterLogItem =
                WaterLogItem((rating * 0.25).toFloat(), "$hour:$minute")
            val waterConsumed = (rating * 0.25).toFloat()

            val userId = loadUID()

            if (userId == null) {
                val i = Intent(applicationContext, LoginActivity::class.java)
                startActivity(i)
                return@setOnClickListener
            }

            updateTrackingLog(userId, waterConsumed, waterLogItem)

            Toast.makeText(
                this,
                "logging ${rating} glasses - ${volume} at $hour:$minute",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        waterRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val volume = if (rating * 250 >= 1000) {
                "${rating * 0.25} L"
            } else {
                "${rating * 250} ml"
            }
            logTxt.text = "logging ${rating} glasses - ${volume}"
        }
    }

    fun loadUID(): String? {
        val currentUser = auth.currentUser
        if (currentUser == null || currentUser.email == null) {
            return null
        }

        var uID: String = sharedPreferences.getString(Configuration.userId, "").toString()
        if (uID == "") {
            val emailRef =
                FirebaseDatabase.getInstance().getReference("Emails").child(
                    convertEmailToKey(
                        currentUser.email!!
                    )
                )
            emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val itemId = dataSnapshot.getValue(String::class.java)
                    if (itemId != null) {
                        uID = itemId
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        return uID
    }

    fun updateTrackingLog(userId: String, waterConsumed: Float, waterLogItem: WaterLogItem) {
        val logRef = FirebaseDatabase.getInstance().getReference("Logs").child(userId)

        logRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val existingLog = dataSnapshot.getValue(TrackingLog::class.java)
                if (existingLog != null) {
                    val updatedLog = existingLog
                    updatedLog.waterConsumed += waterConsumed
                    updatedLog.water_logs.add(waterLogItem)
                    logRef.setValue(updatedLog)
                } else {
                    val newLog = TrackingLog(
                        userId,
                        waterConsumed = waterConsumed,
                        water_logs = mutableListOf(waterLogItem)
                    )
                    logRef.setValue(newLog)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}