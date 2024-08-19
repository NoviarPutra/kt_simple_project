package com.project.location.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultFormText(
  modifier: Modifier = Modifier,
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  hintText: String? = null,
  prefixIcon: (@Composable () -> Unit)? = null,
  suffixIcon: (@Composable () -> Unit)? = null,
  keyboardType: KeyboardType = KeyboardType.Text,
  inputFormatters: List<VisualTransformation> = emptyList(),
  isError: Boolean = false,
  errorMessage: String? = null,
  obscureText: Boolean = false,
  onDone: (() -> Unit)? = null,
) {
  val textColor = if (isError) Color.Red else Color.Black
  val backgroundColor = Color.White
  val borderColor = if (isError) Color.Red else Color.Gray
  val shape = RoundedCornerShape(15.dp)

  Column(modifier = modifier.padding(16.dp)) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(backgroundColor, shape)
    ) {
      TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Gray) },
        placeholder = { hintText?.let { Text(text = it, color = Color.Gray) } },
        leadingIcon = prefixIcon,
        trailingIcon = suffixIcon,
//        visualTransformation = inputFormatters.fold(VisualTransformation.None) { acc, formatter -> acc + formatter },
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = keyboardType,
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            onDone?.invoke()
          }
        ),
        isError = isError,
        modifier = Modifier
          .fillMaxWidth()
          .background(backgroundColor, shape), // Rounded corners
        visualTransformation = if (obscureText) PasswordVisualTransformation() else VisualTransformation.None
      )
    }
  }

//  @Preview(showBackground = true)
//  @Composable
//  fun DefaultFormTextPreview() {
//    Column(modifier = Modifier.padding(16.dp)) {
//      var text = remember { mutableStateOf("") }
//
//      DefaultFormText(
//        value = text,
//        onValueChange = { text = it },
//        label = "Email",
//        hintText = "Enter your email",
//        prefixIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
////        suffixIcon = { Icon(Icons.Filled.Visibility, contentDescription = null) },
//        keyboardType = KeyboardType.Email,
//        inputFormatters = listOf(VisualTransformation.None),
//        isError = text.isEmpty(),
//        errorMessage = if (text.isEmpty()) "Email is required" else null,
//        obscureText = false,
//        onDone = {
//          // Handle the "Done" action, like closing the keyboard
//          // For example, use LocalSoftwareKeyboardController.current?.hide()
//        }
//      )
//    }
}

