package com.project.location.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Geocoder
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ServiceCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.project.location.config.ApiPro2Client
import com.project.location.model.SendLocationResponse
import com.project.location.model.request.LocationRequestData
import com.project.utils.AppUtils
import com.project.utils.NotificationsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class TrackingService : Service() {
  private val binder = LocalBinder()
  private val appUtils = AppUtils(this)
  private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var locationCallback: LocationCallback
  private var timerJob: Job? = null

  private val _locationFlow = MutableStateFlow<Location?>(null)
  val locationFlow: StateFlow<Location?> = _locationFlow

  inner class LocalBinder : Binder() {
    fun getService(): TrackingService = this@TrackingService
  }

  override fun onBind(intent: Intent?): IBinder {
    return binder
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    startAsForegroundService()
    startLocationUpdates()
    coroutineScope.launch {
      withContext(Dispatchers.Main) {
        Toast.makeText(
          this@TrackingService,
          "Service is running...",
          Toast.LENGTH_SHORT
        ).show()
      }
    }
    return START_STICKY
  }

  override fun onCreate() {
    super.onCreate()
    setupLocationUpdates()
    startServiceRunningTicker()
  }

  override fun onDestroy() {
    super.onDestroy()
    fusedLocationClient.removeLocationUpdates(locationCallback)
    timerJob?.cancel()
    coroutineScope.coroutineContext.cancelChildren()
  }

  private fun startAsForegroundService() {
    NotificationsHelper.createNotificationChannel(this)

    val notification = NotificationsHelper.buildNotification(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ServiceCompat.startForeground(
        this,
        1,
        notification,
        ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
      )
    } else {
      startForeground(1, notification)
    }
  }

  fun stopForegroundService() {
    stopSelf()
  }

  private fun setupLocationUpdates() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
          println("Location received: Latitude = ${location.latitude}, Longitude = ${location.longitude}")
          _locationFlow.value = location
          sendLocationToServer(location)
          println("Location received: Latitude = ${location.latitude}, Longitude = ${location.longitude}")
        }
      }
    }
  }

  private fun sendLocationToServer(location: Location) {
    val geocoder = Geocoder(this)
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
    val address = addresses?.get(0)?.getAddressLine(0) ?: "Alamat tidak ditemukan"

    val locationRequest = LocationRequestData(
      userId = appUtils.getUser()?.id.toString(),
      uplinerId = appUtils.getUser()?.uplinerId.toString(),
      latitude = location.latitude.toString(),
      longitude = location.longitude.toString(),
      location = address,
      platform = "android",
      deviceModel = Build.MODEL,
      deviceOs = Build.VERSION.RELEASE
    )

    val apiService = ApiPro2Client.instance.create(LocationApiService::class.java)
    val call = apiService.sendLocationData(locationRequest)

    call.enqueue(object : Callback<SendLocationResponse> {
      override fun onResponse(call: Call<SendLocationResponse>, response: Response<SendLocationResponse>) {
        if (response.isSuccessful) {
          println("Location sent successfully: ${response.body()?.message}")
        } else {
          println("Failed to send location: ${response.errorBody()?.string()}")
        }
      }

      override fun onFailure(call: Call<SendLocationResponse>, t: Throwable) {
        println("Error sending location: ${t.message}")
      }
    })
  }

  private fun startLocationUpdates() {
    val locationRequest = LocationRequest.Builder(LOCATION_UPDATES_INTERVAL_MS)
      .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
      .build()

    if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {

      return
    }
    fusedLocationClient.requestLocationUpdates(
      locationRequest,
      locationCallback,
      Looper.getMainLooper()
    )
  }

  private fun startServiceRunningTicker() {
    timerJob = coroutineScope.launch {
      while (isActive) {
        withContext(Dispatchers.Main) {
          Toast.makeText(
            this@TrackingService,
            "Service is running...",
            Toast.LENGTH_SHORT
          ).show()
        }
        delay(TICKER_PERIOD_SECONDS * 1000L)
      }
    }
  }

  companion object {
    private const val LOCATION_UPDATES_INTERVAL_MS = 10000L
    private const val TICKER_PERIOD_SECONDS = 30L
  }
}
