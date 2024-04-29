package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.caltrack.customviews.ProgressDrawing
import com.example.caltrack.data.InternalStorageHelper
import com.example.caltrack.data.User
import com.example.caltrack.data.TrackingLog

class DashboardActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var heroTxt: TextView
    lateinit var heroDesc: TextView

    lateinit var foodLogBtn: Button
    lateinit var waterLogBtn: Button
    lateinit var historyBtn: Button
    lateinit var alertsBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPreferences = getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        val userData = InternalStorageHelper(this).readFile(
            sharedPreferences.getString(Configuration.name, "") + ".json",
            User()
        ) as User

        val logData = InternalStorageHelper(this).readFile(
            sharedPreferences.getString(Configuration.name, "") + "_log.json",
            TrackingLog()
        ) as TrackingLog

        val customView = ProgressDrawing(this, logData.caloriesConsumed, logData.waterConsumed, userData.dailyCalories.toInt(), userData.dailyWater.toInt())
        val rootView = findViewById<ViewGroup>(R.id.dashboard) // Replace with the ID of your root view
        rootView.addView(customView)

        heroTxt = findViewById(R.id.heroTxt)
        heroTxt.text = """${logData.caloriesConsumed.toInt()} of ${userData.dailyCalories.toInt()} Cal consumed"""

        heroDesc = findViewById(R.id.heroDesc)
        heroDesc.text = """Your maintainence calories are ${userData.dailyCalories + 250}kcal. 
With a calorie deficit of 250kcal a day, you can lose 0.25kg / week and reach your target weight of ${userData.targetWeight}kg in ${userData.weeksNeeded} weeks."""


        foodLogBtn = findViewById(R.id.logfoodBtn)
        waterLogBtn = findViewById(R.id.logWaterBtn)
        historyBtn = findViewById(R.id.historyBtn)
        alertsBtn = findViewById(R.id.alertBtn)

        foodLogBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        waterLogBtn.setOnClickListener {
            val intent = Intent(this, WaterLogActivity::class.java)
            intent.putExtra("name", userData.name)
            intent.putExtra("consumed", logData.waterConsumed)
            intent.putExtra("total", userData.dailyWater)
            startActivity(intent)
        }
        historyBtn.setOnClickListener {
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
        }
        alertsBtn.setOnClickListener {
            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
    }
}