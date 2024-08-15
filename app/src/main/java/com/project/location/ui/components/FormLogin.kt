package com.project.location.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.location.ui.views.LoginView

@Composable
fun FormLogin() {
  val email = remember { mutableStateOf("") }
  val password = remember { mutableStateOf("") }

  Scaffold(
//    topBar = {
//      TopAppBar(
//        title = { Text("Login") },
//      )
//    },
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
          onValueChange = {
            email.value = it
          },
          label = { Text("Email") },
          modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
          value = password.value,
          onValueChange = {
            password.value = it
          },
          label = { Text("Password") },
          modifier = Modifier.fillMaxWidth(0.8f),
          visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = { /* Handle login */ },
          modifier = Modifier.fillMaxWidth(0.8f)
        ) {
          Text("Login")
        }
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  LoginView()
}
