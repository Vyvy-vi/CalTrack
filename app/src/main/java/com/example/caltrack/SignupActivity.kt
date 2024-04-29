package com.example.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class SignupActivity : AppCompatActivity() {
    lateinit var  sharedPreferences: SharedPreferences
    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var password: TextView

    lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        name = findViewById(R.id.etName)
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)

        sharedPreferences = getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        name.text = sharedPreferences.getString(Configuration.name, "")
        email.text = sharedPreferences.getString(Configuration.email, "")
        password.text = sharedPreferences.getString(Configuration.password, "")
        loginLink = findViewById(R.id.loginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun save(view: View?) {
        val name = name.text.toString()
        val email = email.text.toString()
        val password = password.text.toString()

        if (name == "" || email == "" || password == "") {
            Toast.makeText(this,"Fields can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString(Configuration.name, name)
        editor.putString(Configuration.email, email)
        editor.putString(Configuration.password, password)
        editor.putBoolean(Configuration.loggedIn, true)
        editor.apply()

        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    fun clear(view: View) {
        name.text = ""
        email.text = ""
        password.text = ""
    }

    fun get(view: View) {
        sharedPreferences = getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        name.text = sharedPreferences.getString(Configuration.name, "")
        email.text = sharedPreferences.getString(Configuration.email, "")
        password.text = sharedPreferences.getString(Configuration.password, "")
    }
}