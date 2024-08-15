package com.project.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun getInitialScreen(): String {
  val context = LocalContext.current
  val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
  val isLogin = sharedPreferences.getBoolean("isLogin", false)

  return when {
    isLogin -> "tracking"
    else -> "login"
  }

}