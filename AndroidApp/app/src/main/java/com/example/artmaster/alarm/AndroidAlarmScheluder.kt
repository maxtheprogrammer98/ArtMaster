package com.example.artmaster.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId
import java.util.Date

class AndroidAlarmScheluder(
    private val context: Context
): AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val currentTime = System.currentTimeMillis()

        // Check if the selected time is in the past
        if (item.time!!.time <= currentTime) {
            // If so, schedule it for the next occurrence on the following day
            val tomorrow = Date(currentTime + 24 * 60 * 60 * 1000)
            item.time = tomorrow
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_TITLE", item.title)
            putExtra("EXTRA_CONTENT", item.content)
        }

        item.time?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                it.time,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}