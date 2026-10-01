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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.PriorityMediumBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
  isVisible: Boolean,
  editingTask: Task? = null,
  onDismiss: () -> Unit,
  onSaveTask: (
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) -> Unit,
  onDeleteTask: ((String) -> Unit)? = null,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
  if (isVisible) {
    var title by remember(editingTask) { mutableStateOf(editingTask?.title ?: "") }
    var notes by remember(editingTask) { mutableStateOf(editingTask?.notes ?: "") }
    var selectedPriority by remember(editingTask) { mutableStateOf(editingTask?.priority ?: Priority.MEDIUM) }
    var selectedSchedule by remember(editingTask) { mutableStateOf(editingTask?.schedule ?: TaskSchedule.TODAY) }
    var dueDateText by remember(editingTask) { mutableStateOf(editingTask?.dueDateFormatted ?: "") }

    ModalBottomSheet(
      onDismissRequest = onDismiss,
      sheetState = sheetState,
      containerColor = SurfaceWhite,
      shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
      modifier = modifier
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 20.dp, vertical = 8.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TaskVioletBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.CheckBox,
                contentDescription = null,
                tint = TaskViolet,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = if (editingTask == null) "إضافة مهمة جديدة" else "تعديل المهمة",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
              ),
              color = TextPrimary
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Title Input
        Text(
          text = "عنوان المهمة *",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("اكتب ما تريد إنجازه...", color = TextSecondary) },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = TaskViolet
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Schedule Selector (موعد المهمة)
        Text(
          text = "موعد التنفيذ والتاريخ",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          TaskSchedule.entries.forEach { schedule ->
            val isSelected = selectedSchedule == schedule
            val scheduleBg = when {
              isSelected && schedule == TaskSchedule.TODAY -> TaskVioletBg
              isSelected -> Color(0xFFEFF6FF)
              else -> Color(0xFFF8FAFC)
            }
            val scheduleBorder = when {
              isSelected && schedule == TaskSchedule.TODAY -> TaskViolet
              isSelected -> Color(0xFF3B82F6)
              else -> BorderLight
            }
            val scheduleTextColor = when {
              isSelected && schedule == TaskSchedule.TODAY -> TaskViolet
              isSelected -> Color(0xFF1D4ED8)
              else -> TextSecondary
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(scheduleBg)
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = scheduleBorder,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  selectedSchedule = schedule
                  when (schedule) {
                    TaskSchedule.NO_DATE -> dueDateText = ""
                    TaskSchedule.TODAY -> if (dueDateText.isBlank()) dueDateText = com.example.model.getTodayDateString()
                    TaskSchedule.TOMORROW -> dueDateText = com.example.model.getTomorrowDateString()
                    TaskSchedule.FUTURE -> {}
                  }
                }
                .padding(vertical = 10.dp, horizontal = 2.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = schedule.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = scheduleTextColor
              )
            }
          }
        }

        // Custom Due Date Field (if not NO_DATE)
        if (selectedSchedule != TaskSchedule.NO_DATE) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "تاريخ الاستحقاق (صيغة: YYYY/MM/DD)",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = dueDateText,
            onValueChange = { dueDateText = it },
            placeholder = { Text(if (selectedSchedule == TaskSchedule.TODAY) com.example.model.getTodayDateString() else "مثال: 2026/10/05", color = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedBorderColor = BorderLight,
              focusedBorderColor = TaskViolet
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Rule Note about Home visibility
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selectedSchedule == TaskSchedule.TODAY) Color(0xFFF5F3FF) else Color(0xFFF8FAFC))
            .border(
              width = 1.dp,
              color = if (selectedSchedule == TaskSchedule.TODAY) TaskViolet.copy(alpha = 0.4f) else BorderLight,
              shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.Info,
              contentDescription = null,
              tint = if (selectedSchedule == TaskSchedule.TODAY) TaskViolet else TextSecondary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = when (selectedSchedule) {
                TaskSchedule.TODAY -> "⚡ ستظهر هذه المهمة في الصفحة الرئيسية اليوم لأن موعدها مستحق اليوم."
                TaskSchedule.NO_DATE -> "📭 هذه المهمة بدون تاريخ، وستظهر حصراً في صفحة المهام ولن تظهر في الصفحة الرئيسية."
                TaskSchedule.TOMORROW -> "📅 هذه المهمة مجدولة للغد، وستظهر في الصفحة الرئيسية غداً عند استحقاقها."
                TaskSchedule.FUTURE -> "⏳ مجدولة لموعد قادم، ستظهر في صفحة المهام فقط حتى يحل تاريخ تنفيذها."
              },
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                lineHeight = 16.sp
              ),
              color = if (selectedSchedule == TaskSchedule.TODAY) TaskViolet else TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Priority Selector
        Text(
          text = "الأولوية وترتيب الأهمية",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Priority.entries.forEach { priority ->
            val isSelected = selectedPriority == priority
            val (pColor, pBg) = when (priority) {
              Priority.HIGH -> Pair(PriorityHigh, PriorityHighBg)
              Priority.MEDIUM -> Pair(PriorityMedium, PriorityMediumBg)
              Priority.LOW -> Pair(PriorityLow, PriorityLowBg)
              Priority.NONE -> Pair(TextSecondary, Color(0xFFF1F5F9))
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) pBg else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) pColor else BorderLight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { selectedPriority = priority }
                .padding(vertical = 10.dp, horizontal = 2.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = priority.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = if (isSelected) pColor else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Notes / Details Input (Optional)
        Text(
          text = "ملاحظات أو تفاصيل إضافية (اختياري)",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          placeholder = { Text("أي تفاصيل أو روابط تساعدك في تنفيذ المهمة...", color = TextSecondary) },
          maxLines = 3,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = TaskViolet
          )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Actions (Save / Cancel)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              if (title.isNotBlank()) {
                onSaveTask(
                  title.trim(),
                  notes.trim().ifEmpty { null },
                  selectedPriority,
                  selectedSchedule,
                  dueDateText.trim().ifEmpty { null }
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = TaskViolet),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.CheckBox,
              contentDescription = null,
              tint = TextWhite,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (editingTask == null) "إضافة المهمة" else "حفظ التعديلات",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = TextWhite
            )
          }

          TextButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(0.4f)
              .height(48.dp)
          ) {
            Text(
              text = "إلغاء",
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Delete button if editing
        if (editingTask != null && onDeleteTask != null) {
          Spacer(modifier = Modifier.height(10.dp))
          TextButton(
            onClick = {
              onDeleteTask(editingTask.id)
              onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = null,
              tint = Color(0xFFEF4444),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "حذف هذه المهمة نهائياً",
              color = Color(0xFFEF4444),
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
