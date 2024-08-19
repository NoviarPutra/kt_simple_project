package com.project.location.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.location.model.response.LoginErrorResponse
import com.project.location.model.response.LoginResponse
import com.project.location.repository.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: LoginRepository by lazy {
    LoginRepository()
  }

  private val _isLoading = MutableLiveData(false)
  val isLoading: LiveData<Boolean> = _isLoading

  private val _loginResponse = MutableLiveData<LoginResponse?>()
  val loginResponse: LiveData<LoginResponse?> = _loginResponse

  private val _error = MutableLiveData<String?>()
  val error: MutableLiveData<String?> = _error


  fun login(email: String, password: String) {
    viewModelScope.launch {

      if (email.isBlank()) {
        _error.value = "Email cannot be empty"
        viewModelScope.launch {
          delay(2000)
          _error.value = null
        }
        return@launch
      }
      if (password.isBlank()) {
        _error.value = "Password cannot be empty"
        viewModelScope.launch {
          delay(2000)
          _error.value = null
        }
        return@launch
      }
      _isLoading.value = true
      repository.login(email, password).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
          try {
            if (response.isSuccessful) {
              val loginResponse = response.body()
              loginResponse?.let {
                saveLoginData(it)  // Save user and token
                _loginResponse.value = it
              }
            } else {
              val errorBody = response.errorBody()?.string()
              val type = object : TypeToken<LoginErrorResponse>() {}.type
              val errorData = Gson().fromJson<LoginErrorResponse>(errorBody, type)
              _error.value = errorData.message
              viewModelScope.launch {
                delay(2000)
                _error.value = null
              }
            }
            _isLoading.value = false
          } catch (e: Exception) {
            _error.value = e.message
            viewModelScope.launch {
              delay(2000)
              _error.value = null
            }
            _isLoading.value = false
          }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          println("LoginResponse: ${t.message}")
          _error.value = t.message
          viewModelScope.launch {
            delay(2000)
            _error.value = null
          }
          _isLoading.value = false
        }
      })
    }
  }

  private fun saveLoginData(loginResponse: LoginResponse) {
    val sharedPreferences = getApplication<Application>().getSharedPreferences(
      "MyPrefs", Context.MODE_PRIVATE
    )
    with(sharedPreferences.edit()) {
      putBoolean("isLogin", true)
      putString("token", loginResponse.accessToken)
      putString("user", Gson().toJson(loginResponse.user))
      apply()
    }
  }
}


