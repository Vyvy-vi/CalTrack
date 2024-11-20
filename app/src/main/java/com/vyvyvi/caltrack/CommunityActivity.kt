package com.vyvyvi.caltrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val addFriendsBtn = findViewById<Button>(R.id.addFriendsButton)
        val checkFriendsProgressBtn = findViewById<Button>(R.id.checkFriendsStatusButton)
        val readResearchBtn = findViewById<Button>(R.id.readResearchButton)

        addFriendsBtn.setOnClickListener {
        }

        checkFriendsProgressBtn.setOnClickListener {
        }

        readResearchBtn.setOnClickListener {
            val intent = Intent(this, ReadResearchActivity::class.java)
            startActivity(intent)
        }

    }
}