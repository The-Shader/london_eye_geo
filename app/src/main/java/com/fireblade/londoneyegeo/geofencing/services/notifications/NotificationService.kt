package com.fireblade.londoneyegeo.geofencing.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.fireblade.londoneyegeo.GeofenceActivity
import com.fireblade.londoneyegeo.R

// Service class for sending notifications
class NotificationService(val notificationManager: NotificationManager,
                          val resources: Resources,
                          val applicationContext: Context,
                          val activityContext: Context,
                          val appName: String) : INotificationService {

  private val CHANNELID = "ID_01"

  override fun sendNotification(notificationDetails: String) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      // Must set channel on notifications since Android 8.0 (Oreo)
      val channel = NotificationChannel(CHANNELID, appName, NotificationManager.IMPORTANCE_DEFAULT)

      notificationManager.createNotificationChannel(channel)
    }

    val notificationIntent = Intent(applicationContext, GeofenceActivity::class.java)

    val stackBuilder = TaskStackBuilder.create(activityContext)

    stackBuilder.addParentStack(GeofenceActivity::class.java)

    stackBuilder.addNextIntent(notificationIntent)

    val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(activityContext, CHANNELID)

    // Set the UI for the notification
    builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
      .setColor(Color.RED)
      .setContentTitle("Notification:")
      .setContentText(notificationDetails)
      .setContentIntent(notificationPendingIntent)

    builder.setAutoCancel(true)

    notificationManager.notify(0, builder.build())
  }
}