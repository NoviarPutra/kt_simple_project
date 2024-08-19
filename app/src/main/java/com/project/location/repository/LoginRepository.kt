package com.project.location.repository

import com.project.location.config.RetrofitClient
import com.project.location.model.request.LoginRequest
import com.project.location.service.LoginService

class LoginRepository {
  private val service: LoginService by lazy {
    RetrofitClient.instance.create(LoginService::class.java)
  }

  fun login(email: String, password: String) = service.login(
    LoginRequest(email = email, password = password)
  )
}