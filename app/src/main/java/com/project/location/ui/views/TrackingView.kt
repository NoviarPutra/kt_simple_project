package com.project.location.ui.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.location.service.TrackingService
import com.project.location.ui.components.LogoutConfirmationDialog
import com.project.location.viewmodel.TrackingViewModel
import com.project.location.viewmodel.TrackingViewModelFactory
import com.project.utils.AppUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingView(navController: NavController) {
  val context = LocalContext.current
  val viewModels: TrackingViewModel = viewModel(factory = TrackingViewModelFactory(context))


  val location by viewModels.location.observeAsState()
  val address by viewModels.address.observeAsState()
  val permissionGranted = remember { mutableStateOf(false) }
  val isTracking = remember { mutableStateOf(false) }

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    permissionGranted.value = isGranted
    if (isGranted) {
      viewModels.getLastLocation()
    } else {
      Toast.makeText(context, "Location permission is required", Toast.LENGTH_LONG).show()
    }
  }

  val viewModel: TrackingViewModel = viewModel()
  val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()
  val user = AppUtils(context).getUser()

  LaunchedEffect(Unit) {
    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      permissionGranted.value = true
      viewModel.getLastLocation()
    } else {
      launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Welcome, ${user?.fullname}") },
        actions = {
          IconButton(onClick = { viewModel.onLogoutClicked() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
          }
        }
      )
    },
    content = { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        if (permissionGranted.value) {
          Column {
            Text(
              text = location?.let {
                "Location: ${it.latitude}, ${it.longitude}"
              } ?: "Fetching location...",
              fontSize = 14.sp,
              modifier = Modifier.padding(16.dp)
            )
            Text(
              text = address ?: "Fetching address...",
              fontSize = 14.sp,
              modifier = Modifier.padding(16.dp)
            )
          }
        } else {
          Text(
            text = "Location permission is not granted",
            fontSize = 14.sp,
            modifier = Modifier.padding(16.dp)
          )
        }
        Button(
          onClick = {
            if (isTracking.value) {
              val intent = Intent(context, TrackingService::class.java)
              context.stopService(intent)
              isTracking.value = false
            } else {
              val intent = Intent(context, TrackingService::class.java)
              ContextCompat.startForegroundService(context, intent)
              isTracking.value = true
            }
          },
          modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
          Icon(
            imageVector = if (isTracking.value) Icons.Default.Close else Icons.Default.PlayArrow, contentDescription =
            "Start/Stop Tracking"
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = if (isTracking.value) "Stop" else "Start")
        }
      }
    }
  )

  if (showLogoutDialog) {
    LogoutConfirmationDialog(
      isOpen = true,
      onConfirm = {
        viewModel.onLogoutConfirmed {
          // Navigate to the login screen
          navController.navigate("login") {
            popUpTo("tracking") { inclusive = true }
          }
        }
      },
      onCancel = {
        viewModel.onLogoutCanceled()
      }
    )
  }
}

