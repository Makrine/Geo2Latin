package com.makrine.geo2latin2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {
    private const val CHANNEL_ID = "geo2latin_channel"
    private var notificationId = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(CHANNEL_ID, "Geo2Latin Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendLatinNotification(context: Context, chatName: String, latinText: String) {
        createNotificationChannel(context)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val parts = chatName.split(":", limit = 2)
        val groupName = parts.getOrNull(0)?.trim() ?: chatName
        val senderName = parts.getOrNull(1)?.trim()?.removeSuffix(":")

        val notificationTitle = groupName
        val notificationText = if (senderName != null) {
            "$senderName: $latinText"
        } else {
            latinText
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setAutoCancel(true)


        notificationManager.notify(notificationId++, builder.build())
    }

}
