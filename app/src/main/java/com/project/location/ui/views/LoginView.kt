package com.project.location.ui.views


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.project.location.ui.components.FormLogin
import com.project.location.viewmodel.LoginViewModel

@Composable
fun LoginView(navHostController: NavHostController) {
//  val context = LocalContext.current
//  val baseUrl = (context as MainActivity).getEnv("TITLE")
  FormLogin(navHostController)
}