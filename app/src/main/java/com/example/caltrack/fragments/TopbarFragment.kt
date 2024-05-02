package com.example.caltrack.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.caltrack.AlertsActivity
import com.example.caltrack.Configuration
import com.example.caltrack.DashboardActivity
import com.example.caltrack.FoodLogActivity
import com.example.caltrack.LogActivity
import com.example.caltrack.R
import com.example.caltrack.SearchActivity
import com.example.caltrack.SettingActivity
import com.example.caltrack.WaterLogActivity

class TopbarFragment : Fragment() {
    private lateinit var textView: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var settingBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topbar, container, false)
        textView = view.findViewById(R.id.bar_title)
        val activity = requireActivity() as AppCompatActivity
        sharedPreferences =
            activity.getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        if (activity is DashboardActivity) {
            textView.text =
                "Welcome back, " + sharedPreferences.getString(Configuration.name, "friend") + "!"
        } else if (activity is SettingActivity) {
            textView.text = "Settings"
        } else if (activity is WaterLogActivity) {
            textView.text = "Water Log"
        } else if (activity is SearchActivity || activity is FoodLogActivity) {
            textView.text = "Food Log"
        } else if (activity is LogActivity) {
            textView.text = "Log History"
        } else if (activity is AlertsActivity) {
            textView.text = "Alerts"
        }
        settingBtn = view.findViewById(R.id.setting_icon)

        settingBtn.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}