package com.example.caltrack

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlertsActivity : AppCompatActivity() {
    var jobScheduler : JobScheduler? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        val stopAlarmJob = findViewById<Button>(R.id.stop_alarm)
        val startAlarmJob = findViewById<Button>(R.id.start_alarm)

        startAlarmJob.setOnClickListener{
            jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            val componentName = ComponentName(this, AlarmJobService::class.java)
            val builder = JobInfo.Builder(123,componentName)
                .setPersisted(true)
                .setMinimumLatency(2000)
                .setOverrideDeadline(10000)
                //.setPeriodic(900000) // 1800000
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

            jobScheduler!!.schedule(builder.build())
            Toast.makeText(this,"Repeating Water Alarm Set", Toast.LENGTH_SHORT).show()
        }

        stopAlarmJob.setOnClickListener{
            if(jobScheduler != null){
                jobScheduler!!.cancel(123)
                jobScheduler = null
                Toast.makeText(this,"Water Alarm Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}