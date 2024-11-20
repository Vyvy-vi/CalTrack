package com.vyvyvi.caltrack

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var email: TextView
    lateinit var password: TextView
    lateinit var auth  : FirebaseAuth
    lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        auth = FirebaseAuth.getInstance()
        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        email.text = sharedPreferences.getString(Configuration.email, "")
        loginLink = findViewById(R.id.loginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun save(view: View?) {
        val email = email.text.toString()
        val password = password.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString(Configuration.email, email)
        editor.apply()

        register(email, password)
    }

    fun register(_email: String, _password: String) {
        val email = _email
        val password = _password

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}