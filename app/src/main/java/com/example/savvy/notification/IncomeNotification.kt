package com.example.savvy.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.savvy.entities.Income
import java.time.ZoneId
import java.time.ZonedDateTime

fun scheduleNotification(context: Context, income: Income) {
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("title", income.title)
        putExtra("amount", income.amount)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        income.incomeId.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val zonedDateTime = ZonedDateTime.of(income.date.atStartOfDay(), ZoneId.systemDefault())
    val triggerTime = zonedDateTime.toInstant().toEpochMilli()

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        pendingIntent
    )
}