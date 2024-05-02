package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        // sharedPreferences.edit().clear().apply()

        val loggedIn = sharedPreferences.getBoolean(Configuration.loggedIn, false)
        val name = sharedPreferences.getString(Configuration.name, "")
        val email = sharedPreferences.getString(Configuration.email, "")
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val i: Intent
                if (loggedIn) {
                    i = Intent(this, DashboardActivity::class.java)
                } else if (name == "" && email == "") {
                    i = Intent(this, SignupActivity::class.java)
                } else {
                    i = Intent(this, LoginActivity::class.java)
                }
                startActivity(i)
                finish()
            }, 5000
        )
    }
}