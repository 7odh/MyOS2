package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary

@Composable
fun EditNameDialog(
  currentName: String,
  onDismiss: () -> Unit,
  onSave: (String) -> Unit
) {
  var nameInput by remember(currentName) { mutableStateOf(currentName) }

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(20.dp),
    containerColor = SurfaceWhite,
    title = {
      Text(
        text = "تعديل الاسم",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        ),
        color = TextPrimary
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "أدخل اسمك ليظهر في لوحة التحكم الشخصية:",
          style = MaterialTheme.typography.bodyMedium,
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = nameInput,
          onValueChange = { nameInput = it },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (nameInput.isNotBlank()) {
            onSave(nameInput)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("حفظ", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = TextPrimary)
      }
    }
  )
}
