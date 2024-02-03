package com.example.artmaster.tutorials

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // receiving intent from AlarmScheudaler
        val message = intent?.getStringExtra("REMINDER_MESSAGE") ?: return
        // testing
        Log.i("reminder", "$message")

    }
}