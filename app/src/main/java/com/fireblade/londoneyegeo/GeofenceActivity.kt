package com.fireblade.londoneyegeo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fireblade.londoneyegeo.geofencing.GeofenceFragment

class GeofenceActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_geofence)

    // Inject the fragment into the placeholder frame layout
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.fragment_geo, GeofenceFragment(), "GeofenceFragment")
    }.commit()
  }
}
