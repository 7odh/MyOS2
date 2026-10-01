package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AddGoalTaskDialog(
  editingTask: Task? = null,
  onDismiss: () -> Unit,
  onSaveTask: (title: String, notes: String?, priority: Priority, schedule: TaskSchedule, dueDateFormatted: String?) -> Unit
) {
  var title by remember(editingTask) { mutableStateOf(editingTask?.title ?: "") }
  var notes by remember(editingTask) { mutableStateOf(editingTask?.notes ?: "") }
  var selectedPriority by remember(editingTask) { mutableStateOf(editingTask?.priority ?: Priority.HIGH) }
  var selectedSchedule by remember(editingTask) { mutableStateOf(editingTask?.schedule ?: TaskSchedule.TODAY) }
  var dueDateFormatted by remember(editingTask) { mutableStateOf(editingTask?.dueDateFormatted ?: "2025/09/29") }

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(22.dp),
    containerColor = SurfaceWhite,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Outlined.AddTask,
          contentDescription = null,
          tint = BrightBlue,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (editingTask == null) "إضافة مهمة جديدة للهدف" else "تعديل مهمة الهدف",
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
        // Task Title
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("اسم المهمة") },
          placeholder = { Text("مثال: مراجعة القواعد الأساسية", color = TextSecondary) },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Notes (optional)
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("ملاحظات (اختياري)") },
          placeholder = { Text("أضف تفاصيل أو خطوات إضافية...", color = TextSecondary) },
          maxLines = 2,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Priority
        Text(
          text = "الأولوية:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Priority.entries.forEach { priority ->
            val isSelected = selectedPriority == priority
            val activeColor = when (priority) {
              Priority.HIGH -> PriorityHigh
              Priority.MEDIUM -> Color(0xFFD97706)
              Priority.LOW -> PriorityLow
              Priority.NONE -> TextSecondary
            }
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeColor.copy(alpha = 0.12f) else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) activeColor else BorderLight,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedPriority = priority }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = priority.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = if (isSelected) activeColor else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Schedule / Date logic
        Text(
          text = "تاريخ الإنجاز (جدولة المهمة):",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          TaskSchedule.entries.forEach { schedule ->
            val isSelected = selectedSchedule == schedule
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) BrightBlue.copy(alpha = 0.12f) else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) BrightBlue else BorderLight,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedSchedule = schedule }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = schedule.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = if (isSelected) BrightBlue else TextSecondary
              )
            }
          }
        }

        if (selectedSchedule != TaskSchedule.NO_DATE) {
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = dueDateFormatted,
            onValueChange = { dueDateFormatted = it },
            label = { Text("صيغة التاريخ") },
            placeholder = { Text("2025/09/29") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val dateFormatted = if (selectedSchedule == TaskSchedule.NO_DATE) null else dueDateFormatted
            onSaveTask(title.trim(), notes.trim(), selectedPriority, selectedSchedule, dateFormatted)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("حفظ المهمة", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = TextSecondary)
      }
    }
  )
}
