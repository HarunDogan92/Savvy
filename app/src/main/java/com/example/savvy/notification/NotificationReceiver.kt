package com.example.savvy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.R
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        val amount = intent.getIntExtra("amount", 0)

        val builder = NotificationCompat.Builder(context, "savvy")
            .setSmallIcon(R.drawable.notification_icon_background)
            .setContentTitle("Bill Time")
            .setContentText("$title with amount $amount is due")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}