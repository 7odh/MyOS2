package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import com.example.model.Goal
import com.example.model.GoalIconProvider
import com.example.model.GoalStatus
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueTrack
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GoalOverviewCard(
  goal: Goal,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val iconOption = GoalIconProvider.getIcon(goal.iconId)
  val progressAnimated by animateFloatAsState(
    targetValue = (goal.progressPercentage / 100f).coerceIn(0f, 1f),
    animationSpec = tween(500),
    label = "progressAnimated"
  )

  val isCompleted = goal.status == GoalStatus.COMPLETED
  val isPaused = goal.status == GoalStatus.PAUSED

  val (statusText, statusTextColor, statusBgColor) = when (goal.status) {
    GoalStatus.ACTIVE -> Triple("قيد التنفيذ", BrightBlue, Color(0xFFEFF6FF))
    GoalStatus.PAUSED -> Triple("متوقف مؤقتاً", Color(0xFFD97706), Color(0xFFFFFBEB))
    GoalStatus.COMPLETED -> Triple("مكتمل", HabitEmerald, HabitEmeraldBg)
  }

  val activeProgressColor = when {
    isCompleted -> HabitEmerald
    isPaused -> Color(0xFFD97706)
    else -> BrightBlue
  }

  val activeTrackColor = when {
    isCompleted -> HabitEmeraldTrack
    isPaused -> Color(0xFFFEF3C7)
    else -> GoalBlueTrack
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Left side (in RTL: End side): Circular progress indicator with percentage
      CircularProgressRing(
        percentage = goal.progressPercentage,
        strokeColor = activeProgressColor,
        trackColor = activeTrackColor,
        ringSize = 58.dp,
        strokeWidth = 5.dp
      )

      Spacer(modifier = Modifier.width(14.dp))

      // 2. Middle content: Status tag, Goal Title, Progress bar, Task count
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Status Pill Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(statusBgColor)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = statusText,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              ),
              color = statusTextColor
            )
          }

          if (!goal.category.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = goal.category,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Goal Title
        Text(
          text = goal.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          ),
          color = TextPrimary,
          maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Bar
        LinearProgressIndicator(
          progress = { progressAnimated },
          modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = activeProgressColor,
          trackColor = activeTrackColor,
          strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Task count
        Text(
          text = "${goal.completedTasksCount} / ${goal.totalTasksCount} مهام مكتملة",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
          ),
          color = TextSecondary
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // 3. Right side (in RTL: Start side): Goal Icon container
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(iconOption.bgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = iconOption.icon,
          contentDescription = goal.title,
          tint = iconOption.tintColor,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Navigation arrow
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = "عرض التفاصيل",
        tint = TextSecondary.copy(alpha = 0.5f),
        modifier = Modifier.size(14.dp)
      )
    }
  }
}
