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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Habit
import com.example.model.HabitType
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.extraColors

@Composable
fun HabitItemRow(
  habit: Habit,
  onIncrementCounter: () -> Unit,
  onOpenLogDialog: () -> Unit,
  onToggleBoolean: () -> Unit,
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val isDark = extra.isDark
  val isCompleted = habit.isCompleted
  val isExceeded = habit.currentValue > habit.targetValue

  val rowBorderColor by animateColorAsState(
    targetValue = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else extra.border,
    animationSpec = tween(durationMillis = 200),
    label = "rowBorder"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = if (isDark) 0.dp else 2.dp,
        shape = RoundedCornerShape(16.dp),
        ambientColor = extra.shadow,
        spotColor = extra.shadow
      )
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, rowBorderColor, RoundedCornerShape(16.dp))
      .clickable {
        when (habit.type) {
          HabitType.COUNTER -> onIncrementCounter()
          HabitType.QUANTITY, HabitType.DURATION -> onOpenLogDialog()
          HabitType.BOOLEAN -> onToggleBoolean()
        }
      },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCompleted) {
        if (isDark) Color(0xFF064E3B).copy(alpha = 0.25f) else Color(0xFFF0FDF4)
      } else {
        extra.cardSurface
      }
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Right side (RTL start): Habit Icon in soft rounded badge
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(if (isCompleted) extra.habitBg else extra.secondarySurface),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = habit.iconEmoji,
          fontSize = 20.sp
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
              fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.SemiBold,
              fontSize = 14.5.sp
            ),
            color = extra.textPrimary
          )

          // Progress label text
          val progressLabel = when (habit.type) {
            HabitType.BOOLEAN -> if (isCompleted) "مكتملة ✓" else "لم تكتمل"
            else -> "${habit.currentValue} / ${habit.targetValue} ${habit.unit}"
          }

          Text(
            text = progressLabel,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            color = when {
              isExceeded -> HabitEmerald
              isCompleted -> HabitEmerald
              else -> extra.textSecondary
            }
          )
        }

        // Linear Progress bar
        if (habit.type != HabitType.BOOLEAN) {
          Spacer(modifier = Modifier.height(7.dp))
          LinearProgressIndicator(
            progress = {
              if (habit.targetValue <= 0) 0f
              else (habit.currentValue.toFloat() / habit.targetValue.toFloat()).coerceIn(0f, 1f)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(5.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = if (isCompleted) HabitEmerald else BrandPrimary,
            trackColor = if (isCompleted) extra.habitTrack else extra.border,
            strokeCap = StrokeCap.Round
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // 3. Left side (RTL end): Interactive Action (+1 pill button, log button, or checkmark)
      when (habit.type) {
        HabitType.COUNTER -> {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isCompleted) extra.habitBg else extra.secondarySurface)
              .border(
                width = 1.dp,
                color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrandPrimary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(12.dp)
              )
              .clickable(onClick = onIncrementCounter)
              .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (isCompleted) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = HabitEmerald,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
              }
              Text(
                text = "+1",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                ),
                color = if (isCompleted) HabitEmerald else BrandPrimary
              )
            }
          }
        }
        HabitType.QUANTITY, HabitType.DURATION -> {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isCompleted) extra.habitBg else extra.secondarySurface)
              .border(
                width = 1.dp,
                color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrandPrimary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(12.dp)
              )
              .clickable(onClick = onOpenLogDialog)
              .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (isCompleted) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = HabitEmerald,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
              }
              Text(
                text = if (habit.type == HabitType.DURATION) "وقت ⏱️" else "+1",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                ),
                color = if (isCompleted) HabitEmerald else BrandPrimary
              )
            }
          }
        }
        HabitType.BOOLEAN -> {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(if (isCompleted) HabitEmerald else Color.Transparent)
              .border(
                2.dp,
                if (isCompleted) HabitEmerald else extra.border,
                CircleShape
              )
              .clickable(onClick = onToggleBoolean),
            contentAlignment = Alignment.Center
          ) {
            if (isCompleted) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "مكتملة",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }
}
