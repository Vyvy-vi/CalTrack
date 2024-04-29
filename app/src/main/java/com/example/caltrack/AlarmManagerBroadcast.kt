package com.example.caltrack


import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.media.MediaPlayer


import android.util.Log
import android.widget.Toast


class AlarmManagerBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var mp = MediaPlayer.create(context, R.raw.alarm)
        Log.d("Drink Water", "repeating alarm for water reminder")
        mp.start()
        Toast.makeText(context, "Drink Water!", Toast.LENGTH_LONG).show()
    }
}