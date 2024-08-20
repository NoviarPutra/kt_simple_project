package com.project.location.ui.views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.*
import com.project.utils.getInitialScreen

@Composable
fun MainApp() {
  val navController = rememberNavController()
  val initialScreen = getInitialScreen()
  val focusManager = LocalFocusManager.current

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = {
            focusManager.clearFocus()
          }
        )
      }
  ) {
    NavHost(navController = navController, startDestination = initialScreen) {
      composable("login") { LoginView(navController) }
      composable("tracking") {
        TrackingView(navController)
      }
    }
  }
}