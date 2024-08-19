package com.project.location.service

import com.project.location.model.SendLocationResponse
import com.project.location.model.request.LocationRequestData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApiService {
  @POST("hadir-service/last-tracking")
  fun sendLocationData(@Body locationRequest: LocationRequestData): Call<SendLocationResponse>
}