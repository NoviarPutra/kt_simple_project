package com.project.location.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LogoutConfirmationDialog(
  isOpen: Boolean,
  onConfirm: () -> Unit,
  onCancel: () -> Unit,
) {
  if (isOpen) {
    AlertDialog(
      onDismissRequest = { onCancel() },
      title = { Text("Confirm Logout") },
      text = { Text("Are you sure you want to logout?") },
      confirmButton = {
        TextButton(onClick = onConfirm) {
          Text("Logout")
        }
      },
      dismissButton = {
        TextButton(onClick = onCancel) {
          Text("Cancel")
        }
      }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun LogoutConfirmationDialogPreview() {
  LogoutConfirmationDialog(
    isOpen = true,
    onConfirm = {},
    onCancel = {}
  )
}