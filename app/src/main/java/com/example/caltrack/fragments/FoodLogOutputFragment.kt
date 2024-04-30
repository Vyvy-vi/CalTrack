package com.example.caltrack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.caltrack.R

class FoodLogOutputFragment : Fragment() {

    lateinit var txtData: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_food_log_output, container, false)
        txtData = v.findViewById(R.id.macros)
        return v
    }

    fun displayReceivedData(message: String) {
        txtData.setText(message)
    }
}