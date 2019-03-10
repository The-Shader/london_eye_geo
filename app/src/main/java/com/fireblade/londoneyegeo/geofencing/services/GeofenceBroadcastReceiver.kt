package com.fireblade.londoneyegeo.geofencing.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Receiver for geofence transition events. Responds to Intents from the Location Services
// which are containing the type of the transition and the IDs of the triggered geofences.
class GeofenceBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {

    GeofenceTransitionsIntentService.enqueueWork(
      context,
      intent
    )
  }
}