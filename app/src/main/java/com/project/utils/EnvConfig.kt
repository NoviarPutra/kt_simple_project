package com.project.utils

import android.content.Context
import java.io.IOException
import java.util.Properties

object EnvConfig {
  private lateinit var dotenv: Properties

  fun initialize(context: Context) {
    if (!::dotenv.isInitialized) {
      dotenv = "env".loadEnvFile(context)
    }
  }

  fun getEnv(key: String): String {
    return dotenv.getProperty(key) ?: "DEFAULT"
  }

  private fun String.loadEnvFile(context: Context): Properties {
    val properties = Properties()
    try {
      val inputStream = context.assets.open(this)
      properties.load(inputStream)
    } catch (e: IOException) {
      e.printStackTrace()
      throw IOException("Error loading env file", e)
    }
    return properties
  }
}