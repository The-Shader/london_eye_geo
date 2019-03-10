package com.fireblade.londoneyegeo.geofencing

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fireblade.londoneyegeo.R
import com.fireblade.londoneyegeo.geofencing.model.GeofenceLandmarks
import com.fireblade.londoneyegeo.geofencing.services.GeofenceBroadcastReceiver
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_geofence.*

class GeofenceFragment : Fragment(), OnCompleteListener<Void> {

  private val REQUEST_PERMISSIONS = 2

  lateinit var geofencingClient: GeofencingClient

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Create the View from the fragment_geofence layout resource file
    return inflater.inflate(R.layout.fragment_geofence, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Initialize the client for geofencing calls
    geofencingClient = LocationServices.getGeofencingClient(context!!)

    // Check whether the user granted access to the fine location permission: continue execution if possible, prompt the user for access otherwise
    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      status_text.text = getString(R.string.geofence_stopped)
      // Handling permission-requests in Fragments are only available from Android 6.0 (Marshmallow),
      // use the activity or FragmentCompat for earlier versions
      requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS)
    }
    else {
      geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).addOnCompleteListener(this)
    }
  }

  private fun getGeofencingRequest(): GeofencingRequest {

    return GeofencingRequest.Builder().apply {
      // Trigger GEOFENCE_TRANSITION_ENTER transition event when the user is within the location at the start of the application
      setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
      addGeofences(GeofenceLandmarks.landMarks)
    }.build()
  }

  private val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(context.applicationContext, GeofenceBroadcastReceiver::class.java)
    PendingIntent.getBroadcast(context.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

    if (requestCode == REQUEST_PERMISSIONS && grantResults.first() == PackageManager.PERMISSION_GRANTED) {

      // Add the geofences if the permissions are granted
      // Even though the IDE warns for a possible error, it's pointless to check for the permission that has just been granted
      geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).addOnCompleteListener(this)
    }

    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  override fun onComplete(task: Task<Void>) {
    // Update the status based on the result of the addition of the geofences to the geofence client
    if (task.isSuccessful && ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      status_text.text = getString(R.string.geofence_running)
      val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0.0f, geofencePendingIntent)
    }
    else {
      status_text.text = getString(R.string.geofence_stopped)
    }
  }
}