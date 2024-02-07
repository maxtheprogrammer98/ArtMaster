package com.example.artmaster.tutorials

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime
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

        // extracting values
        val date = item.date
        val time = item.time
        //testing
        Log.i("alarmTest", "date: $date")
        Log.i("alarmTest", "time : $time")

        // TODO: fix conversion problems
        // converting values
        val localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .withHour(time.hour)
            .withMinute(time.minute)
            .withSecond(0)

        val alarmTimeMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        //testing
        Log.i("alarmTest", "time in millis: $alarmTimeMillis")
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