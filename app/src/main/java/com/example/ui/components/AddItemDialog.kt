package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.QuickAddType
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AddItemDialog(
  type: QuickAddType,
  onDismiss: () -> Unit,
  onAddTask: (String, Priority) -> Unit,
  onAddHabit: (String, Priority) -> Unit,
  onAddGoal: (String, List<String>, Priority) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var subStepsText by remember { mutableStateOf("") }
  var selectedPriority by remember { mutableStateOf(Priority.HIGH) }

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(22.dp),
    containerColor = SurfaceWhite,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(type.accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = type.icon,
            contentDescription = type.titleArabic,
            tint = type.accentColor,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = type.titleArabic,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = TextPrimary
        )
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("العنوان") },
          placeholder = {
            Text(
              when (type) {
                QuickAddType.TASK -> "مثال: مراجعة البريد الهام"
                QuickAddType.HABIT -> "مثال: المشي الصباحي (30 دقيقة)"
                QuickAddType.GOAL -> "مثال: تعلم لغة جديدة"
                QuickAddType.NOTE -> "عنوان الملاحظة"
                QuickAddType.PROJECT -> "اسم المشروع"
                QuickAddType.LIST -> "اسم القائمة"
                QuickAddType.EVENT -> "اسم الحدث"
                QuickAddType.FILE -> "اسم الملف"
              }
            )
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        )

        if (type == QuickAddType.GOAL) {
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = subStepsText,
            onValueChange = { subStepsText = it },
            label = { Text("المهام الفرعية (مفصولة بفاصلة)") },
            placeholder = { Text("خطوة 1, خطوة 2, خطوة 3") },
            maxLines = 3,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "الأولوية:",
          style = MaterialTheme.typography.labelMedium,
          color = TextSecondary
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Priority.entries.forEach { priority ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.clickable { selectedPriority = priority }
            ) {
              RadioButton(
                selected = selectedPriority == priority,
                onClick = { selectedPriority = priority }
              )
              Text(
                text = priority.titleArabic,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            when (type) {
              QuickAddType.TASK -> onAddTask(title, selectedPriority)
              QuickAddType.HABIT -> onAddHabit(title, selectedPriority)
              QuickAddType.GOAL -> {
                val steps = if (subStepsText.isNotBlank()) {
                  subStepsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                } else {
                  listOf("الخطوة الأولى")
                }
                onAddGoal(title, steps, selectedPriority)
              }
              else -> onAddTask(title, selectedPriority)
            }
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = type.accentColor),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("إضافة", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = TextPrimary)
      }
    }
  )
}
