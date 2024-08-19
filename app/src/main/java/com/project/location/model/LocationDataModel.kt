package com.project.location.model

import com.google.gson.annotations.SerializedName

data class LocationDataModel(
  @SerializedName("latitude") val latitude: Double,
  @SerializedName("longitude") val longitude: Double,
  @SerializedName("address") val address: String?,
)