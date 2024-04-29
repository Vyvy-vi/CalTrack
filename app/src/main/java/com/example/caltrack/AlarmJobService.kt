package com.example.caltrack
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log

class AlarmJobService: JobService() {
    lateinit var pendingIntent: PendingIntent
    lateinit var intent: Intent

    @SuppressLint("ServiceCast")
    override fun onStartJob(params: JobParameters?):Boolean{
        Log.d("TAG","onStartJob:")

        intent = Intent(this, AlarmManagerBroadcast::class.java)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        pendingIntent = PendingIntent.getBroadcast(this,234,intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pendingIntent)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("TAG","onStopJob:")
        return true
    }
}