package com.vyvyvi.caltrack.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vyvyvi.caltrack.Configuration
import com.vyvyvi.caltrack.LoginActivity
import com.vyvyvi.caltrack.R
import com.vyvyvi.caltrack.data.InternalStorageHelper
import com.vyvyvi.caltrack.data.LogAdapter
import com.vyvyvi.caltrack.data.TrackingLog
import com.vyvyvi.caltrack.utils.Utils.convertEmailToKey

class WaterLogsFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var logsList: RecyclerView
    private lateinit var adapter: LogAdapter
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.fragment_water_logs, container, false)

        val context = requireContext().applicationContext
        sharedPreferences =
            context.getSharedPreferences(Configuration.sharedPreferences, Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        logsList = v.findViewById<RecyclerView>(R.id.log_list)
        layoutManager = LinearLayoutManager(context)
        logsList.layoutManager = layoutManager

        getLogData { logData ->

            if (logData == null) {
                val i = Intent(context, LoginActivity::class.java)
                startActivity(i)
                return@getLogData
            }

            adapter = LogAdapter(logData.water_logs)
            logsList.adapter = adapter
        }


        return v
    }

    fun loadUID(): String? {
        val currentUser = auth.currentUser
        if (currentUser == null || currentUser.email == null) {
            return null
        }

        var uID: String = sharedPreferences.getString(Configuration.userId, "").toString()
        if (uID == "") {
            val emailRef =
                FirebaseDatabase.getInstance().getReference("Emails").child(
                    convertEmailToKey(
                        currentUser.email!!
                    )
                )
            emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val itemId = dataSnapshot.getValue(String::class.java)
                    if (itemId != null) {
                        uID = itemId
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        return uID
    }


    fun getLogData(callback: (TrackingLog?) -> Unit) {
        val uID = loadUID()
        if (uID == null) {
            callback(null)
            return
        }

        val dbRef =
            FirebaseDatabase.getInstance().getReference("Logs").child(uID)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var logData = dataSnapshot.getValue(TrackingLog::class.java)
                if (logData != null) {
                    callback(logData)
                } else {
                    logData = TrackingLog()
                    dbRef.setValue(logData)
                }
                callback(logData)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}