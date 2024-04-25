package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var  sharedPreferences: SharedPreferences
    val myPreferences = "calTrackLogin"
    val _name="namekey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val name = sharedPreferences.getString(_name, "")
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val i: Intent
                i = if (name == "") {
                    Intent(this, LoginActivity::class.java)
                } else {
                    Intent(this, DashboardActivity::class.java)
                }
                startActivity(i)
                finish()
            },5000
        )
    }
}