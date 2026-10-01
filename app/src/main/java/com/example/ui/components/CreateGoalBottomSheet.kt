package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.TrackChanges
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
import com.example.model.Goal
import com.example.model.GoalIconOption
import com.example.model.GoalIconProvider
import com.example.model.Priority
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.PriorityMediumBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalBottomSheet(
  isVisible: Boolean,
  editingGoal: Goal? = null,
  onDismiss: () -> Unit,
  onSaveGoal: (title: String, description: String?, iconId: String, priority: Priority, dueDate: String?) -> Unit,
  onDeleteGoal: ((String) -> Unit)? = null,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
  if (isVisible) {
    var title by remember(editingGoal) { mutableStateOf(editingGoal?.title ?: "") }
    var description by remember(editingGoal) { mutableStateOf(editingGoal?.description ?: "") }
    var selectedIconId by remember(editingGoal) { mutableStateOf(editingGoal?.iconId ?: "target") }
    var selectedPriority by remember(editingGoal) { mutableStateOf(editingGoal?.priority ?: Priority.HIGH) }
    var dueDate by remember(editingGoal) { mutableStateOf(editingGoal?.dueDate ?: "2025/12/31") }

    ModalBottomSheet(
      onDismissRequest = onDismiss,
      sheetState = sheetState,
      containerColor = SurfaceWhite,
      shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
      modifier = modifier
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
          .navigationBarsPadding()
          .verticalScroll(rememberScrollState())
      ) {
        // Sheet Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Outlined.TrackChanges,
              contentDescription = null,
              tint = BrightBlue,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (editingGoal == null) "إضافة هدف جديد" else "تعديل الهدف",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = TextPrimary
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Goal Title
        Text(
          text = "اسم الهدف",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("أدخل اسم الهدف", color = TextSecondary) },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Goal Description (Optional)
        Text(
          text = "الوصف (اختياري)",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          placeholder = { Text("أضف وصفاً مختصراً عن هدفك...", color = TextSecondary) },
          maxLines = 3,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Icon Selector
        Text(
          text = "اختر أيقونة",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          GoalIconProvider.defaultIcons.forEach { iconOption ->
            val isSelected = iconOption.id == selectedIconId
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isSelected) iconOption.bgColor else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) iconOption.tintColor else BorderLight,
                  shape = RoundedCornerShape(14.dp)
                )
                .clickable { selectedIconId = iconOption.id },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = iconOption.icon,
                contentDescription = iconOption.nameArabic,
                tint = if (isSelected) iconOption.tintColor else TextSecondary,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Priority Selection
        Text(
          text = "الأولوية",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          PriorityPill(
            title = "عالية",
            isSelected = selectedPriority == Priority.HIGH,
            activeColor = PriorityHigh,
            activeBg = PriorityHighBg,
            onClick = { selectedPriority = Priority.HIGH },
            modifier = Modifier.weight(1f)
          )
          PriorityPill(
            title = "متوسطة",
            isSelected = selectedPriority == Priority.MEDIUM,
            activeColor = Color(0xFFD97706),
            activeBg = Color(0xFFFFFBEB),
            onClick = { selectedPriority = Priority.MEDIUM },
            modifier = Modifier.weight(1f)
          )
          PriorityPill(
            title = "منخفضة",
            isSelected = selectedPriority == Priority.LOW,
            activeColor = PriorityLow,
            activeBg = PriorityLowBg,
            onClick = { selectedPriority = Priority.LOW },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Due Date (Optional)
        Text(
          text = "تاريخ الاستحقاق (اختياري)",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = dueDate,
          onValueChange = { dueDate = it },
          placeholder = { Text("مثال: 2025/12/31", color = TextSecondary) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Outlined.CalendarMonth,
              contentDescription = null,
              tint = BrightBlue
            )
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              if (title.isNotBlank()) {
                onSaveGoal(
                  title.trim(),
                  description.trim(),
                  selectedIconId,
                  selectedPriority,
                  dueDate.trim()
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.TrackChanges,
              contentDescription = null,
              tint = TextWhite,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (editingGoal == null) "إنشاء الهدف" else "حفظ التعديلات",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          TextButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(0.5f)
              .height(48.dp)
          ) {
            Text(
              text = "إلغاء",
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        if (editingGoal != null && onDeleteGoal != null) {
          Spacer(modifier = Modifier.height(10.dp))
          TextButton(
            onClick = {
              onDeleteGoal(editingGoal.id)
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
              text = "حذف هذا الهدف نهائياً",
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

@Composable
private fun PriorityPill(
  title: String,
  isSelected: Boolean,
  activeColor: Color,
  activeBg: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isSelected) activeBg else Color(0xFFF8FAFC))
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) activeColor else BorderLight,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(onClick = onClick)
      .padding(vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodySmall.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        fontSize = 13.sp
      ),
      color = if (isSelected) activeColor else TextSecondary
    )
  }
}
