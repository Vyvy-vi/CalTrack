package com.vyvyvi.caltrack.fragments

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
import com.vyvyvi.caltrack.AlertsActivity
import com.vyvyvi.caltrack.CommunityActivity
import com.vyvyvi.caltrack.Configuration
import com.vyvyvi.caltrack.DashboardActivity
import com.vyvyvi.caltrack.FoodLogActivity
import com.vyvyvi.caltrack.LogActivity
import com.vyvyvi.caltrack.R
import com.vyvyvi.caltrack.ReadResearchActivity
import com.vyvyvi.caltrack.SearchActivity
import com.vyvyvi.caltrack.SettingActivity
import com.vyvyvi.caltrack.StepsActivity
import com.vyvyvi.caltrack.WaterLogActivity

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
        } else if (activity is CommunityActivity) {
            textView.text = "Community"
        } else if (activity is StepsActivity) {
            textView.text = "Step Tracker"
        }

        settingBtn = view.findViewById(R.id.setting_icon)

        settingBtn.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}