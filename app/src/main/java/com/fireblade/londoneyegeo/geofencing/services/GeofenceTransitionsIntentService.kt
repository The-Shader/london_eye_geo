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
import com.fireblade.londoneyegeo.geofencing.services.notifications.INotificationService
import com.fireblade.londoneyegeo.geofencing.services.notifications.NotificationService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsIntentService : JobIntentService() {

  private val notificationService: INotificationService by lazy {
    NotificationService(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
      resources, applicationContext, baseContext, getString(R.string.app_name))
  }

  companion object {

    private const val JOB_ID = 1000

    // Helper function for
    fun enqueueWork(context: Context, intent: Intent) {
      enqueueWork(context, GeofenceTransitionsIntentService::class.java,
        JOB_ID, intent)
    }
  }

  override fun onHandleWork(intent: Intent) {
    val geofencingEvent = GeofencingEvent.fromIntent(intent)

    if (geofencingEvent.hasError()) {
      notificationService.sendNotification("Notification Error")
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
        notificationService.sendNotification("$geofenceName $geofenceTransitionDetails")
      }
    }
  }
}