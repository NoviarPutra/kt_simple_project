package com.project.location.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.location.viewmodel.LoginViewModel
import com.project.location.viewmodel.LoginViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormLogin(navController: NavHostController) {
  val context = LocalContext.current
  val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))

  val email = remember { mutableStateOf("") }
  val password = remember { mutableStateOf("") }
  val isLoading by viewModel.isLoading.observeAsState(false)
  val error by viewModel.error.observeAsState()
  val loginResponse by viewModel.loginResponse.observeAsState()
  LaunchedEffect(loginResponse) {
    loginResponse?.let {
      navController.navigate("tracking") {
        popUpTo("login") { inclusive = true }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(title = { Text(text = "Login") })
    },
    content = { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        TextField(
          value = email.value,
          leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          onValueChange = {
            email.value = it
          },
          label = { Text("Email") },
          modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
          value = password.value,
          leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
          trailingIcon = { Text(text = "Show  ") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          onValueChange = {
            password.value = it
          },
          label = { Text("Password") },
          modifier = Modifier.fillMaxWidth(0.8f),
          visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = { viewModel.login(email.value, password.value) },
          modifier = Modifier.fillMaxWidth(0.8f)
        ) {
          Text("Login")
        }

        error?.let { errorMessage ->
          Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 16.sp,
            modifier = Modifier
              .padding(16.dp)
              .background(Color.White, shape = RoundedCornerShape(8.dp))
              .border(1.dp, Color.Red, shape = RoundedCornerShape(8.dp))
              .padding(16.dp)
          )
        }
      }
    }
  )
  if (isLoading) {
    LoadingIndicator(true)
  }
}

