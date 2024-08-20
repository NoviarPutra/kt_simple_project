package com.project.location.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

@Suppress("DEPRECATION")
class TrackingViewModel(application: Application) : AndroidViewModel(application) {
  private val fusedLocationClient: FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(application)
  private val geocoder: Geocoder = Geocoder(application, Locale.getDefault())

  private val _location = MutableLiveData<Location?>()
  val location: LiveData<Location?> = _location

  private val _address = MutableLiveData<String?>()
  val address: LiveData<String?> = _address


  fun getLastLocation() {
    val context = getApplication<Application>()
    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
          _location.value = location
          location?.let { loc ->
            getAddressFromLocation(loc)
          }
        }
        .addOnFailureListener { exception ->
          // Handle any exceptions
          println("Error getting location: ${exception.message}")
          _location.value = null
        }
    }
  }

  private fun getAddressFromLocation(location: Location) {
    try {
      val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
      if (addresses?.isNotEmpty() == true) {
        val address = addresses[0]
        _address.value = address.getAddressLine(0) // Get the full address
      } else {
        _address.value = "No address found"
      }
    } catch (e: Exception) {
      _address.value = "Error getting address"
    }
  }


  private val _showLogoutDialog = MutableStateFlow(false)
  val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

  fun onLogoutClicked() {
    _showLogoutDialog.value = true
  }

  fun onLogoutConfirmed(navigateToLogin: () -> Unit) {
    clearLocalStorage()
    navigateToLogin()
    _showLogoutDialog.value = false
  }

  fun onLogoutCanceled() {
    _showLogoutDialog.value = false
  }

  private fun clearLocalStorage() {
    val sharedPreferences = getApplication<Application>().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
      clear()
      apply()
    }
  }


}