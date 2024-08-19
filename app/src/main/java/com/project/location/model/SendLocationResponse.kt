package com.project.location.model

import com.google.gson.annotations.SerializedName

data class SendLocationResponse(
  @SerializedName("success") val success: Boolean,
  @SerializedName("message") val message: String,
)