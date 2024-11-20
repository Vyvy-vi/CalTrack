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

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth : FirebaseAuth
    lateinit var email: TextView
    lateinit var password: TextView

    lateinit var signupLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)

        auth = FirebaseAuth.getInstance()

        sharedPreferences =
            getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        email.text = sharedPreferences.getString(Configuration.email, "")

        signupLink = findViewById(R.id.signupLink)
        signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
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
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }
}