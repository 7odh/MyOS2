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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.model.Habit
import com.example.ui.theme.BorderLight
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

data class PauseDurationOption(
  val days: Int?,
  val title: String,
  val subtitle: String
)

private val PAUSE_OPTIONS = listOf(
  PauseDurationOption(3, "3 أيام", "استراحة قصيرة لالتقاط الأنفاس"),
  PauseDurationOption(7, "أسبوع واحد (7 أيام)", "تخفيف الضغط خلال أسبوع مزدحم"),
  PauseDurationOption(14, "أسبوعان (14 يوماً)", "فترة مناسبة أثناء السفر أو الامتحانات"),
  PauseDurationOption(30, "شهر كامل (30 يوماً)", "تجميد مؤقت للتركيز على أولويات أخرى"),
  PauseDurationOption(null, "حتى الاستئناف يدوياً", "ركن العادة لأجل غير مسمى مع حفظ السجل")
)

@Composable
fun PauseHabitDialog(
  habit: Habit,
  onDismiss: () -> Unit,
  onConfirmPause: (days: Int?) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedDays by remember { mutableStateOf<Int?>(7) } // Default to 1 week

  AlertDialog(
    onDismissRequest = onDismiss,
    shape = RoundedCornerShape(24.dp),
    containerColor = SurfaceWhite,
    modifier = modifier,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFFEF3C7)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.PauseCircleOutline,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "ركن العادة مؤقتاً",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "${habit.iconEmoji} ${habit.title}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = TextSecondary
          )
        }
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Informational banner about preserving history & streaks
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0FDF4))
            .border(1.dp, HabitEmerald.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Outlined.Shield,
              contentDescription = null,
              tint = HabitEmerald,
              modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "سجلك وسلسلة أيامك (Streak: ${habit.currentStreak} يوم 🔥) محفوظة بالكامل ولن تتأثر أو تُحذف. ستختفي العادة من لوحة اليوم مؤقتاً لتقليل الضغوط.",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                lineHeight = 16.sp
              ),
              color = Color(0xFF166534)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "حدد مدة ركن العادة:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Duration radio list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          PAUSE_OPTIONS.forEach { option ->
            val isSelected = selectedDays == option.days
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) Color(0xFFFFFBEB) else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) Color(0xFFD97706) else BorderLight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { selectedDays = option.days }
                .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Radio indicator
                Box(
                  modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFD97706) else Color.Transparent)
                    .border(
                      width = 1.5.dp,
                      color = if (isSelected) Color(0xFFD97706) else BorderLight,
                      shape = CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  if (isSelected) {
                    Box(
                      modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                    )
                  }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Text(
                    text = option.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 13.sp
                    ),
                    color = if (isSelected) Color(0xFFB45309) else TextPrimary
                  )
                  Text(
                    text = option.subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextSecondary
                  )
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirmPause(selectedDays) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("تأكيد ركن العادة ⏸️", fontWeight = FontWeight.Bold, color = TextWhite)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = TextSecondary)
      }
    }
  )
}
