package com.vyvyvi.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        FirebaseDatabase.getInstance();

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        val email = sharedPreferences.getString(Configuration.email, "")
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val i: Intent
                if (currentUser != null) {
                    i = Intent(this, DashboardActivity::class.java)
                } else if (email == "") {
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