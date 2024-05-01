package com.example.caltrack.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caltrack.Configuration
import com.example.caltrack.DashboardActivity
import com.example.caltrack.R
import com.example.caltrack.api.FoodNutritionDetails
import com.example.caltrack.api.SearchResultsAdapter
import com.example.caltrack.data.FoodLogItem
import com.example.caltrack.data.InternalStorageHelper
import com.example.caltrack.data.LogAdapter
import com.example.caltrack.data.TrackingLog
import java.time.LocalDateTime

class FoodLogsFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var logsList: RecyclerView
    private lateinit var adapter: LogAdapter
    private lateinit var layoutManager: LinearLayoutManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_food_logs, container, false)

        val context = requireContext().applicationContext
        sharedPreferences =
            context.getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)

        val name = sharedPreferences.getString(Configuration.name, "")
        val logData = getActivity()?.let {
            InternalStorageHelper(it.applicationContext).readFile(
                sharedPreferences.getString(Configuration.name, "") + "_log.json",
                TrackingLog()
            )
        } as TrackingLog

        logsList = v.findViewById<RecyclerView>(R.id.log_list)
        layoutManager = LinearLayoutManager(context)
        logsList.layoutManager = layoutManager
        adapter = LogAdapter(logData.food_logs)
        logsList.adapter = adapter


        return v
    }
}