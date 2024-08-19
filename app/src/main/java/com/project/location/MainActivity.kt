package com.project.location

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.project.location.service.TrackingService
import com.project.location.ui.theme.LocationTheme
import com.project.location.ui.views.MainApp
import com.project.utils.EnvConfig
import java.io.IOException
import java.util.Properties

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Check if TrackingService is running
    val isServiceRunning = isServiceRunning(TrackingService::class.java, this)
    if (isServiceRunning) {
      Log.d("MainActivity", "TrackingService is running")
    } else {
      Log.d("MainActivity", "TrackingService is not running")
    }

    // Initialize environment configuration
    EnvConfig.initialize(this)

    // Enable edge-to-edge display
    enableEdgeToEdge()

    // Set content for the activity
    setContent {
      LocationTheme {
        MainApp()
      }
    }
  }

  private fun isServiceRunning(serviceClass: Class<out Service>, context: Context): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = manager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
      if (serviceClass.name == service.service.className) {
        return true
      }
    }
    return false
  }

}
