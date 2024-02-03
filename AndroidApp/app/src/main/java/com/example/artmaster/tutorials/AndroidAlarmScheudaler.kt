package com.example.artmaster.tutorials

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AndroidAlarmScheudaler(
    private val context : Context
): AlarmScheudaler{

    // setting alarmManager
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheudale(item: AlarmItem) {
        // generating intent
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("REMINDER_MESSAGE", item.message)
        }
        // setting reminder
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
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