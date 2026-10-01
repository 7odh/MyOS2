package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.TrackChanges
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
import com.example.model.Task
import com.example.ui.theme.BorderLight
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.GoalBlueTrack
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun GoalCardItem(
  goal: Goal,
  onGoalClick: () -> Unit,
  onToggleGoalTask: (taskId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = (goal.progressPercentage / 100f).coerceIn(0f, 1f),
    animationSpec = tween(durationMillis = 500),
    label = "goalProgress"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(18.dp)),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Main Goal Header Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onGoalClick),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Goal Icon
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(GoalBlueBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.TrackChanges,
            contentDescription = "الهدف",
            tint = GoalBlue,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Progress Bar
        Column(
          modifier = Modifier.weight(1f)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = goal.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              ),
              color = TextPrimary
            )
            Text(
              text = "${goal.completedTasksCount}/${goal.totalTasksCount}",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              ),
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(5.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = GoalBlue,
            trackColor = GoalBlueTrack,
            strokeCap = StrokeCap.Round
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Navigation Chevron
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = "عرض تفاصيل الهدف",
          tint = TextSecondary.copy(alpha = 0.7f),
          modifier = Modifier.size(14.dp)
        )
      }

      // Goal Tasks Chips (Horizontal row of task steps)
      if (goal.tasks.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          goal.tasks.forEach { task ->
            GoalTaskChip(
              task = task,
              onToggle = { onToggleGoalTask(task.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun GoalTaskChip(
  task: Task,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val chipBgColor = if (task.isCompleted) Color(0xFFF0FDF4) else Color(0xFFF8FAFC)
  val chipBorderColor = if (task.isCompleted) HabitEmerald.copy(alpha = 0.5f) else BorderLight

  Row(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(chipBgColor)
      .border(1.dp, chipBorderColor, RoundedCornerShape(16.dp))
      .clickable(onClick = onToggle)
      .padding(horizontal = 10.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Check indicator
    Box(
      modifier = Modifier
        .size(16.dp)
        .clip(CircleShape)
        .background(if (task.isCompleted) HabitEmerald else Color.Transparent)
        .border(
          1.dp,
          if (task.isCompleted) HabitEmerald else BorderLight,
          CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      if (task.isCompleted) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "مكتملة",
          tint = TextWhite,
          modifier = Modifier.size(11.dp)
        )
      }
    }

    Spacer(modifier = Modifier.width(6.dp))

    Text(
      text = task.title,
      style = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp,
        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium
      ),
      color = if (task.isCompleted) HabitEmerald else TextPrimary
    )
  }
}
