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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Habit
import com.example.model.HabitType
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LogHabitProgressDialog(
  habit: Habit,
  onDismiss: () -> Unit,
  onSaveProgress: (newTotalValue: Int) -> Unit
) {
  var addAmountText by remember { mutableStateOf("") }
  var runningTotal by remember(habit) { mutableStateOf(habit.currentValue) }

  val isExceeded = runningTotal > habit.targetValue
  val isCompleted = runningTotal >= habit.targetValue

  val quickAmounts = when (habit.type) {
    HabitType.DURATION -> listOf(5, 10, 15, 30)
    HabitType.QUANTITY -> listOf(1, 2, 5, 10)
    HabitType.COUNTER -> listOf(1, 2, 3, 5)
    HabitType.BOOLEAN -> listOf(1)
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(22.dp),
    containerColor = SurfaceWhite,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = habit.iconEmoji,
          fontSize = 22.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = habit.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "تسجيل الإنجاز اليومي",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondary
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Question
        Text(
          text = "عملت قد إيه من المطلوب منك النهارده؟",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          ),
          color = TextPrimary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Progress status card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCompleted) HabitEmeraldBg else Color(0xFFF8FAFC))
            .border(
              width = 1.dp,
              color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BorderLight,
              shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "التقدم الحالي:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
              Text(
                text = "$runningTotal من ${habit.targetValue} ${habit.unit}",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                ),
                color = if (isCompleted) HabitEmerald else BrightBlue
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
              progress = {
                if (habit.targetValue <= 0) 0f
                else (runningTotal.toFloat() / habit.targetValue.toFloat()).coerceIn(0f, 1f)
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = if (isCompleted) HabitEmerald else BrightBlue,
              trackColor = if (isCompleted) HabitEmeraldTrack else Color(0xFFE2E8F0),
              strokeCap = StrokeCap.Round
            )

            if (isExceeded) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "تجاوزت المطلوب اليوم بمقدار ${runningTotal - habit.targetValue} ${habit.unit}! رائع جداً 🎉",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = HabitEmerald
              )
            } else if (isCompleted) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "اكتمل الهدف المطلوب لليوم! أحسنت ✨",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = HabitEmerald
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick addition chips
        Text(
          text = "إضافة سريعة:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextSecondary,
          modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          quickAmounts.forEach { amount ->
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEFF6FF))
                .border(1.dp, BrightBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable {
                  runningTotal += amount
                }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "+$amount ${habit.unit}",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = BrightBlue
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom amount input
        OutlinedTextField(
          value = addAmountText,
          onValueChange = { input ->
            if (input.all { it.isDigit() }) {
              addAmountText = input
            }
          },
          label = { Text("أو أضف مقداراً محدداً") },
          placeholder = { Text("مثال: 10", color = TextSecondary) },
          trailingIcon = {
            if (addAmountText.isNotBlank()) {
              Button(
                onClick = {
                  val added = addAmountText.toIntOrNull() ?: 0
                  if (added > 0) {
                    runningTotal += added
                    addAmountText = ""
                  }
                },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                modifier = Modifier.padding(end = 4.dp)
              ) {
                Text("إضافة", fontSize = 11.sp)
              }
            }
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderLight,
            focusedBorderColor = BrightBlue
          )
        )

        // Reset button if count > 0
        if (runningTotal > 0) {
          Spacer(modifier = Modifier.height(8.dp))
          TextButton(
            onClick = { runningTotal = 0 }
          ) {
            Text(
              text = "إعادة تعيين اليوم إلى صفر",
              color = Color(0xFFEF4444),
              fontSize = 11.sp
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSaveProgress(runningTotal)
        },
        colors = ButtonDefaults.buttonColors(containerColor = HabitEmerald),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("حفظ الإنجاز", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = TextSecondary)
      }
    }
  )
}
