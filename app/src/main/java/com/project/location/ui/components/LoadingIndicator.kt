package com.project.location.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingIndicator(isLoading: Boolean, text: String = "Loading...") {
  if (isLoading) Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Color.Black.copy(
          alpha = 0.1f
        )
      ),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(50.dp),
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(text = text, color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center)
    }
  }
}