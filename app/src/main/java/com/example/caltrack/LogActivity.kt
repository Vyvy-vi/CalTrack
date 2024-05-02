package com.example.caltrack

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.caltrack.fragments.AllLogsFragment
import com.example.caltrack.fragments.FoodLogsFragment
import com.example.caltrack.fragments.WaterLogsFragment


class LogActivity : AppCompatActivity() {

    lateinit var allBtn: Button
    lateinit var foodBtn: Button
    lateinit var waterBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        foodBtn = findViewById(R.id.food_logs)
        allBtn = findViewById(R.id.all_logs)
        waterBtn = findViewById(R.id.water_logs)

        foodBtn.setOnClickListener {
            loadFragment("food")
        }
        allBtn.setOnClickListener {
            loadFragment("all")
        }
        waterBtn.setOnClickListener {
            loadFragment("water")
        }
        loadFragment("all")
    }

    private fun loadFragment(filter: String) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transactionTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val containerView = findViewById<FrameLayout>(R.id.log_fragment_frame)

        if (containerView != null) {
            val fragment: Fragment

            if (filter == "food") {
                fragment = FoodLogsFragment()
            } else if (filter == "water") {
                fragment = WaterLogsFragment()
            } else {
                fragment = AllLogsFragment()
            }

            transactionTransaction.replace(containerView.id, fragment)
            transactionTransaction.commit()
        }
    }
}