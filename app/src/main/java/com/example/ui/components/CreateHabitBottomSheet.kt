package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DayOfWeekArabic
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
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

private val DEFAULT_HABIT_EMOJIS = listOf(
  "🕌", "💧", "📖", "🏋️‍♂️", "⌨️", "💪", "🏃‍♂️", "🧘‍♂️",
  "💊", "🌙", "📚", "💻", "🥗", "✍️", "🧹", "🌱"
)

private val UNIT_SUGGESTIONS = listOf(
  "صفحات", "عدات", "نطات", "دقيقة", "صلوات", "أكواب", "كلمات", "مرات", "كيلومتر"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateHabitBottomSheet(
  isVisible: Boolean,
  editingHabit: Habit? = null,
  onDismiss: () -> Unit,
  onSaveHabit: (
    title: String,
    type: HabitType,
    targetValue: Int,
    unit: String,
    frequency: HabitFrequency,
    scheduledDays: List<DayOfWeekArabic>,
    isMandatory: Boolean,
    priority: Priority,
    iconEmoji: String
  ) -> Unit,
  onDeleteHabit: ((String) -> Unit)? = null,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
  if (isVisible) {
    var title by remember(editingHabit) { mutableStateOf(editingHabit?.title ?: "") }
    var selectedType by remember(editingHabit) { mutableStateOf(editingHabit?.type ?: HabitType.BOOLEAN) }
    var targetValueText by remember(editingHabit) {
      mutableStateOf(editingHabit?.targetValue?.toString() ?: "1")
    }
    var unitText by remember(editingHabit) { mutableStateOf(editingHabit?.unit ?: "مرة") }
    var frequency by remember(editingHabit) { mutableStateOf(editingHabit?.frequency ?: HabitFrequency.DAILY) }
    var selectedDays by remember(editingHabit) {
      mutableStateOf(editingHabit?.scheduledDays ?: listOf(DayOfWeekArabic.SATURDAY, DayOfWeekArabic.MONDAY, DayOfWeekArabic.THURSDAY))
    }
    var isMandatory by remember(editingHabit) { mutableStateOf(editingHabit?.isMandatory ?: false) }
    var selectedPriority by remember(editingHabit) { mutableStateOf(editingHabit?.priority ?: Priority.HIGH) }
    var selectedEmoji by remember(editingHabit) { mutableStateOf(editingHabit?.iconEmoji ?: "🌱") }

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
          Text(
            text = if (editingHabit == null) "إضافة عادة جديدة" else "تعديل العادة",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            ),
            color = TextPrimary
          )
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Emoji Picker
        Text(
          text = "أيقونة العادة",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          DEFAULT_HABIT_EMOJIS.forEach { emoji ->
            val isSelected = selectedEmoji == emoji
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) HabitEmeraldBg else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) HabitEmerald else BorderLight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { selectedEmoji = emoji },
              contentAlignment = Alignment.Center
            ) {
              Text(text = emoji, fontSize = 20.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Title Field
        Text(
          text = "اسم العادة *",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("مثال: قراءة القرآن، الجيم، شرب الماء...", color = TextSecondary) },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = HabitEmerald
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Habit Type Selection (طريقة القياس)
        Text(
          text = "طريقة قياس وحساب العادة",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          HabitType.entries.forEach { type ->
            val isSelected = selectedType == type
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) HabitEmeraldBg else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) HabitEmerald else BorderLight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  selectedType = type
                  when (type) {
                    HabitType.BOOLEAN -> {
                      targetValueText = "1"
                      unitText = "مرة"
                    }
                    HabitType.QUANTITY -> {
                      if (targetValueText == "1") targetValueText = "10"
                      if (unitText == "مرة") unitText = "صفحة"
                    }
                    HabitType.DURATION -> {
                      if (targetValueText == "1") targetValueText = "30"
                      unitText = "دقيقة"
                    }
                    HabitType.COUNTER -> {
                      if (targetValueText == "1") targetValueText = "5"
                      if (unitText == "مرة") unitText = "صلوات"
                    }
                  }
                }
                .padding(vertical = 10.dp, horizontal = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = type.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = if (isSelected) HabitEmerald else TextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Target value and Custom Unit fields (if not BOOLEAN)
        if (selectedType != HabitType.BOOLEAN) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Target Value
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (selectedType == HabitType.DURATION) "المدة المطلوبة (دقائق)" else "الكمية / العدد المطلوب",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(4.dp))
              OutlinedTextField(
                value = targetValueText,
                onValueChange = { input ->
                  if (input.all { it.isDigit() }) targetValueText = input
                },
                placeholder = { Text("مثال: 10") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  unfocusedBorderColor = BorderLight,
                  focusedBorderColor = HabitEmerald
                )
              )
            }

            // Custom Unit Name
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "اسم الوحدة (تسمية القياس)",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextSecondary
              )
              Spacer(modifier = Modifier.height(4.dp))
              OutlinedTextField(
                value = unitText,
                onValueChange = { unitText = it },
                placeholder = { Text("مثال: صفحات، عدات...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  unfocusedBorderColor = BorderLight,
                  focusedBorderColor = HabitEmerald
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Unit suggestions chips
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            UNIT_SUGGESTIONS.forEach { suggestion ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (unitText == suggestion) HabitEmeraldBg else Color(0xFFF1F5F9))
                  .border(
                    width = 0.5.dp,
                    color = if (unitText == suggestion) HabitEmerald else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                  )
                  .clickable { unitText = suggestion }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = suggestion,
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                  color = if (unitText == suggestion) HabitEmerald else TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Frequency (التكرار والأيام)
        Text(
          text = "التكرار ومواعيد الأيام",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          HabitFrequency.entries.forEach { freq ->
            val isSelected = frequency == freq
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) HabitEmeraldBg else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) HabitEmerald else BorderLight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { frequency = freq }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = freq.titleArabic,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 13.sp
                ),
                color = if (isSelected) HabitEmerald else TextSecondary
              )
            }
          }
        }

        // Days selector if SPECIFIC_DAYS
        AnimatedVisibility(visible = frequency == HabitFrequency.SPECIFIC_DAYS) {
          Column {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "اختر الأيام المحددة في الأسبوع (مثال: سبت، اثنين، خميس للجيم)",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              DayOfWeekArabic.entries.forEach { day ->
                val isDaySelected = selectedDays.contains(day)
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isDaySelected) HabitEmerald else Color(0xFFF1F5F9))
                    .clickable {
                      selectedDays = if (isDaySelected) {
                        selectedDays - day
                      } else {
                        selectedDays + day
                      }
                    }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = day.shortArabic,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    ),
                    color = if (isDaySelected) TextWhite else TextSecondary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Mandatory vs Rest Mode Interaction
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isMandatory) Color(0xFFF0FDF4) else Color(0xFFF8FAFC))
            .border(
              width = 1.dp,
              color = if (isMandatory) HabitEmerald.copy(alpha = 0.5f) else BorderLight,
              shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isMandatory) "🛡️ عادة إجبارية (لا تتأثر بزر الراحة)" else "☕ عادة تتأثر بوضع الراحة",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  ),
                  color = if (isMandatory) HabitEmerald else TextPrimary
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isMandatory)
                  "تظل نشطة دائماً حتى لو ضغطت على زر راحة (مثل الصلوات والأذكار)."
                else
                  "عند تفعيل زر راحة، يُسجل اليوم كيوم راحة مستحق وتختفي من الرئيسية لراحتك دون كسر السلسلة.",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                color = TextSecondary
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Switch(
              checked = isMandatory,
              onCheckedChange = { isMandatory = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = HabitEmerald,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color(0xFFE2E8F0)
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Priority Selector
        Text(
          text = "الأولوية",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
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
            activeColor = PriorityMedium,
            activeBg = PriorityMediumBg,
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

        Spacer(modifier = Modifier.height(24.dp))

        // 7. Actions (Save / Cancel / Delete)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              if (title.isNotBlank()) {
                val targetVal = targetValueText.toIntOrNull() ?: 1
                onSaveHabit(
                  title.trim(),
                  selectedType,
                  targetVal,
                  unitText.trim().ifEmpty { "مرة" },
                  frequency,
                  selectedDays,
                  isMandatory,
                  selectedPriority,
                  selectedEmoji
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = HabitEmerald),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Spa,
              contentDescription = null,
              tint = TextWhite,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (editingHabit == null) "إضافة العادة" else "حفظ التعديلات",
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

        // Delete button if editing
        if (editingHabit != null && onDeleteHabit != null) {
          Spacer(modifier = Modifier.height(10.dp))
          TextButton(
            onClick = {
              onDeleteHabit(editingHabit.id)
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
              text = "حذف هذه العادة نهائياً",
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
