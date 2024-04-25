package com.example.caltrack

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    lateinit var  sharedPreferences: SharedPreferences
    lateinit var name: TextView
    lateinit var password: TextView
    lateinit var currentWeight: TextView
    lateinit var targetWeight: TextView

    val myPreferences = "calTrackLogin"
    val _name="namekey"
    val _password="emailKey"
    val _currentWeight = "currentWeightKey"
    val _targetWeight = "targetWeightKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        name = findViewById(R.id.etName)
        password = findViewById(R.id.etPassword)
        currentWeight = findViewById(R.id.etCurrentWeight)
        targetWeight = findViewById(R.id.etTargetWeight)

        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        name.text = sharedPreferences.getString(_name, "")
        password.text = sharedPreferences.getString(_password, "")
        currentWeight.text = sharedPreferences.getString(_currentWeight, "")
        targetWeight.text = sharedPreferences.getString(_targetWeight, "")
    }

    fun save(view: View?) {
        val editor = sharedPreferences.edit()
        editor.putString(_name, name.text.toString())
        editor.putString(_password, password.text.toString())
        editor.putString(_currentWeight, currentWeight.text.toString())
        editor.putString(_targetWeight, targetWeight.text.toString())
        editor.apply()
    }

    fun clear(view: View) {
        name.text = ""
        password.text = ""
        currentWeight.text = ""
        targetWeight.text = ""
    }

    fun get(view: View) {
        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        name.text = sharedPreferences.getString(_name, "")
        password.text = sharedPreferences.getString(_password, "")
        currentWeight.text = sharedPreferences.getString(_currentWeight, "")
        targetWeight.text = sharedPreferences.getString(_targetWeight, "")
    }
}