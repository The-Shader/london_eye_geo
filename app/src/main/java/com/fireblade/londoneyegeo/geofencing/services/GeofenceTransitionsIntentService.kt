package com.fireblade.londoneyegeo.geofencing.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import com.fireblade.londoneyegeo.GeofenceActivity
import com.fireblade.londoneyegeo.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsIntentService : JobIntentService() {

  companion object {

    private const val JOB_ID = 1000

    private val CHANNELID = "ID_01"

    // Helper function for
    fun enqueueWork(context: Context, intent: Intent) {
      enqueueWork(context, GeofenceTransitionsIntentService::class.java,
        JOB_ID, intent)
    }
  }

  override fun onHandleWork(intent: Intent) {
    val geofencingEvent = GeofencingEvent.fromIntent(intent)

    if (geofencingEvent.hasError()) {
      sendNotification("Notification Error")
      return
    }

    var geofenceTransitionDetails = ""

    when(geofencingEvent.geofenceTransition) {
      Geofence.GEOFENCE_TRANSITION_ENTER -> geofenceTransitionDetails = "round and round we go"
      Geofence.GEOFENCE_TRANSITION_EXIT -> geofenceTransitionDetails = "time to go home..."
      Geofence.GEOFENCE_TRANSITION_DWELL -> geofenceTransitionDetails = "round and round we go"
    }

    // Filter for the valid transitions
    if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
      geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
      geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

      // Iterate over the triggered geofences and push a notification for each
      geofencingEvent.triggeringGeofences.map {
        val geofenceName = it.requestId
        sendNotification("$geofenceName $geofenceTransitionDetails")
      }
    }
  }

  private fun sendNotification(notificationDetails: String) {

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      val appName = getString(R.string.app_name)

      // Must set channel on notifications since Android 8.0 (Orea)
      val channel = NotificationChannel(CHANNELID, appName, NotificationManager.IMPORTANCE_DEFAULT)

      notificationManager.createNotificationChannel(channel)
    }

    val notificationIntent = Intent(applicationContext, GeofenceActivity::class.java)

    val stackBuilder = TaskStackBuilder.create(baseContext)

    stackBuilder.addParentStack(GeofenceActivity::class.java)

    stackBuilder.addNextIntent(notificationIntent)

    val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(baseContext, CHANNELID)

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