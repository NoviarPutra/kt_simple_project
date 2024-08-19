package com.project.location.model.response

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(
  @SerializedName("name") val name: String,
  @SerializedName("message") val message: String,
  @SerializedName("code") val code: Int,
  @SerializedName("className") val className: String,
  @SerializedName("errors") val errors: Map<String, Any>,
)
