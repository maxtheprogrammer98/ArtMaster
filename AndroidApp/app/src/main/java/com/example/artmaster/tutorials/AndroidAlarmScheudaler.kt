package com.example.artmaster.tutorials

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar

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
        // extracting values
        val date = item.date
        val time = item.time
        // converting values
        val calendar = Calendar.getInstance().apply {
            // date
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.month)
            set(Calendar.DAY_OF_MONTH, date.day)
            // time
            set(Calendar.HOUR, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 1)
        }
        val alarmTimeMillis = calendar.timeInMillis
        // setting reminder
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeMillis,
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