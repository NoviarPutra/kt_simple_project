package com.project.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.project.location.R

object NotificationsHelper {

  private const val CHANNEL_ID = "tracking_service_channel"
  private const val CHANNEL_NAME = "Tracking Service Channel"
  private const val CHANNEL_DESCRIPTION = "Channel for tracking service notifications"

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW
      ).apply {
        description = CHANNEL_DESCRIPTION
      }
      val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun buildNotification(context: Context): Notification {
    return NotificationCompat.Builder(context, CHANNEL_ID)
      .setContentTitle("Tracking Service")
      .setContentText("Tracking your location")
      .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this resource exists
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setOngoing(true) // Optional: makes the notification persistent
      .build()
  }
}