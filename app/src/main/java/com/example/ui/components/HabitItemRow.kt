package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Habit
import com.example.model.HabitType
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun HabitItemRow(
  habit: Habit,
  onIncrementCounter: () -> Unit,
  onOpenLogDialog: () -> Unit,
  onToggleBoolean: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isCompleted = habit.isCompleted
  val isExceeded = habit.currentValue > habit.targetValue

  val rowBorderColor by animateColorAsState(
    targetValue = if (isCompleted) HabitEmerald.copy(alpha = 0.4f) else BorderLight.copy(alpha = 0.6f),
    animationSpec = tween(durationMillis = 200),
    label = "rowBorder"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .border(1.dp, rowBorderColor, RoundedCornerShape(14.dp))
      .clickable {
        when (habit.type) {
          HabitType.COUNTER -> onIncrementCounter()
          HabitType.QUANTITY, HabitType.DURATION -> onOpenLogDialog()
          HabitType.BOOLEAN -> onToggleBoolean()
        }
      },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCompleted) Color(0xFFF9FDFB) else SurfaceCard
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Right side (RTL start): Habit Icon in soft rounded badge
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(if (isCompleted) HabitEmeraldBg else Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = habit.iconEmoji,
          fontSize = 18.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // 2. Middle content: Title & Progress Bar / Text
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = habit.title,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Medium,
              fontSize = 14.sp
            ),
            color = TextPrimary
          )

          // Current vs Target text badge
          val progressLabel = when (habit.type) {
            HabitType.BOOLEAN -> if (isCompleted) "مكتملة ✓" else "لم تكتمل"
            else -> "${habit.currentValue} / ${habit.targetValue} ${habit.unit}"
          }

          Text(
            text = progressLabel,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = when {
              isExceeded -> HabitEmerald
              isCompleted -> HabitEmerald
              else -> TextSecondary
            }
          )
        }

        // Linear Progress bar for non-boolean habits
        if (habit.type != HabitType.BOOLEAN) {
          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = {
              if (habit.targetValue <= 0) 0f
              else (habit.currentValue.toFloat() / habit.targetValue.toFloat()).coerceIn(0f, 1f)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = if (isCompleted) HabitEmerald else BrightBlue,
            trackColor = if (isCompleted) HabitEmeraldTrack else Color(0xFFE2E8F0),
            strokeCap = StrokeCap.Round
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // 3. Left side (RTL end): Interactive Action (Counter button, Log button, or Checkbox)
      when (habit.type) {
        HabitType.COUNTER -> {
          // Increment pill button (e.g. +1 cup or +1 prayer)
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(if (isCompleted) HabitEmeraldBg else Color(0xFFEFF6FF))
              .border(
                width = 1.dp,
                color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrightBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
              )
              .clickable(onClick = onIncrementCounter)
              .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (isCompleted) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = HabitEmerald,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
              }
              Text(
                text = "+1",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                ),
                color = if (isCompleted) HabitEmerald else BrightBlue
              )
            }
          }
        }
        HabitType.QUANTITY, HabitType.DURATION -> {
          // Quick Log button ("تسجيل" with edit icon)
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(if (isCompleted) HabitEmeraldBg else Color(0xFFEFF6FF))
              .border(
                width = 1.dp,
                color = if (isCompleted) HabitEmerald.copy(alpha = 0.4f) else BrightBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
              )
              .clickable(onClick = onOpenLogDialog)
              .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (isCompleted) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = HabitEmerald,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
              }
              Text(
                text = if (habit.type == HabitType.DURATION) "تسجيل وقت" else "تسجيل إنجاز",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                color = if (isCompleted) HabitEmerald else BrightBlue
              )
            }
          }
        }
        HabitType.BOOLEAN -> {
          // Single-tap custom check circle
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(if (isCompleted) HabitEmerald else Color.Transparent)
              .border(
                1.5.dp,
                if (isCompleted) HabitEmerald else BorderLight,
                CircleShape
              )
              .clickable(onClick = onToggleBoolean),
            contentAlignment = Alignment.Center
          ) {
            if (isCompleted) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "مكتملة",
                tint = TextWhite,
                modifier = Modifier.size(15.dp)
              )
            }
          }
        }
      }
    }
  }
}
