package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    lateinit var  sharedPreferences: SharedPreferences
    lateinit var name: TextView
    lateinit var password: TextView
    lateinit var currentWeight: TextView
    lateinit var targetWeight: TextView

    lateinit var signupLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        name = findViewById(R.id.etName)
        password = findViewById(R.id.etPassword)

        sharedPreferences = getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        name.text = sharedPreferences.getString(Configuration.name, "")

        signupLink = findViewById(R.id.signupLink)
        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    fun save(view: View?) {
        val name = name.text.toString()
        val password = password.text.toString()

        if (name == "" || password == "") {
            Toast.makeText(this,"Fields can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (
            (sharedPreferences.getString(Configuration.name, "") != name)
            ) {
            Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()
        }
        else if (
            (sharedPreferences.getString(Configuration.password, "") != password)
        ) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()
            return
        } else {

            val editor = sharedPreferences.edit()
            editor.putBoolean(Configuration.loggedIn, true)
            editor.apply()

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun clear(view: View) {
        name.text = ""
        password.text = ""
        currentWeight.text = ""
        targetWeight.text = ""
    }

    fun get(view: View) {
        sharedPreferences = getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        name.text = sharedPreferences.getString(Configuration.name, "")
        password.text = sharedPreferences.getString(Configuration.password, "")
    }
}