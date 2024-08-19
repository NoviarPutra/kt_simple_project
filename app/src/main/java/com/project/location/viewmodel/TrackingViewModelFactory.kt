package com.project.location.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(TrackingViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return TrackingViewModel(context.applicationContext as Application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}