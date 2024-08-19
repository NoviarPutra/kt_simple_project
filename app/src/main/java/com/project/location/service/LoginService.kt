package com.project.location.service

import com.project.location.model.request.LoginRequest
import com.project.location.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
  @POST("authentication")
  fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}