package com.project.utils

import android.content.Context
import com.google.gson.Gson
import com.project.location.model.UserModel

class AppUtils(private val context: Context) {
  fun getUser(): UserModel? {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val userJson = sharedPreferences.getString("user", null)
    return if (!userJson.isNullOrEmpty()) {
      Gson().fromJson(userJson, UserModel::class.java)
    } else {
      null
    }
  }
}