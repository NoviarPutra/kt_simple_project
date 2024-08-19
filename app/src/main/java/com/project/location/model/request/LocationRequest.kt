package com.project.location.model.request

import com.google.gson.annotations.SerializedName

data class LocationRequestData(
  @SerializedName("user_id") val userId: String,
  @SerializedName("upliner_id") val uplinerId: String,
  @SerializedName("latitude") val latitude: String,
  @SerializedName("longitude") val longitude: String,
  @SerializedName("location") val location: String,
  @SerializedName("platform") val platform: String,
  @SerializedName("device_model") val deviceModel: String,
  @SerializedName("device_os") val deviceOs: String,
)
