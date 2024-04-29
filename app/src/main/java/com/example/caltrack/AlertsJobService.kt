package com.example.caltrack
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log

class AlertsJobService: JobService() {
    @SuppressLint("ServiceCast")
    override fun onStartJob(params: JobParameters?):Boolean{
        Log.d("TAG","onStartJob:")
        val intent = Intent(this, AlarmManagerBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,234,intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1800,pendingIntent) // 1800000
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("TAG","onStopJob:")
        return true
    }
}