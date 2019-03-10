package com.fireblade.londoneyegeo.geofencing.model

import com.google.android.gms.location.Geofence

class GeofenceLandmarks {

  companion object {
    // The list of landmarks with their latitude, longitude, and circular radius of interest
    val landMarks : ArrayList<Geofence> by lazy {
      arrayListOf<Geofence>(Geofence.Builder().setRequestId("LondonEye").setCircularRegion(51.503399, -0.119519, 100.0f)
        .setExpirationDuration(Geofence.NEVER_EXPIRE) // unset expiration interval for the location
        .setLoiteringDelay(500) // set a delay in ms between posting enter and dwell transitions
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
        .build(),
        Geofence.Builder().setRequestId("Olympic Park").setCircularRegion(51.54615, -0.01269, 1600.0f)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
          .setLoiteringDelay(500)
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
          .build(),
        Geofence.Builder().setRequestId("Westfield").setCircularRegion(51.545935, -0.009248, 1600.0f)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
          .setLoiteringDelay(500)
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
          .build())
    }
  }
}