package com.example.caltrack


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build


import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat


class AlarmManagerBroadcast : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel : NotificationChannel

    private val channelId = "water-alerts"
    private val description = "Water Notification"
    private val title = "Water Notification"
    private lateinit var builder : Notification.Builder

    val notificationId = 1234

    lateinit var pendingIntent: PendingIntent
    lateinit var new_intent: Intent
    lateinit var supportAction: Notification.Action
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Water Alarm", "notification for water reminder")
        new_intent = Intent(context, WaterLogActivity::class.java)
        pendingIntent = PendingIntent.getActivity(context,0,new_intent,
            PendingIntent.FLAG_IMMUTABLE)
        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        supportAction = Notification.Action.Builder(R.drawable.ic_alerts, "Support", pendingIntent).build()
        myNotificationChannel(context)
        notificationManager.notify(notificationId, builder.build())


        var mp = MediaPlayer.create(context, R.raw.minecraft_alarm)
        Log.d("Water Reminder", "alarm for water reminder")
        mp.start()
        Toast.makeText(context, "Drink Water!", Toast.LENGTH_LONG).show()
    }
    private fun myNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            Log.d("Notification Channel", "new notification channel created")

            builder = Notification.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.ic_waterlog))
                .setContentIntent(pendingIntent)
                .addAction(supportAction)
                .setAutoCancel(true)

        }
        else{
            builder = Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.ic_waterlog))
                .setContentIntent(pendingIntent)
                .addAction(supportAction)
                .setAutoCancel(true)

        }
    }
}