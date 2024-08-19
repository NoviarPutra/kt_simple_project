package com.project.location.model.response

import com.google.gson.annotations.SerializedName
import com.project.location.model.UserModel

data class LoginResponse(
  @SerializedName("user") val user: UserModel,
  @SerializedName("accessToken") val accessToken: String,
)
