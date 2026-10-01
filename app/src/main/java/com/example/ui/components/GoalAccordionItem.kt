package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Goal
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.theme.BorderLight
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun GoalAccordionItem(
  goal: Goal,
  isExpanded: Boolean,
  onToggleExpand: () -> Unit,
  onToggleGoalTask: (taskId: String) -> Unit,
  onPostponeGoalTask: ((taskId: String) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val rotationAngle by animateFloatAsState(
    targetValue = if (isExpanded) 180f else 0f,
    animationSpec = tween(durationMillis = 250),
    label = "accordionChevronRotation"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // 1. Goal Header (Clicking toggles accordion expansion ONLY)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onToggleExpand)
          .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Goal Icon
        val iconOption = com.example.model.GoalIconProvider.getIcon(goal.iconId)
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(iconOption.bgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = iconOption.icon,
            contentDescription = goal.title,
            tint = iconOption.tintColor,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Goal Title & Category
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = goal.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            ),
            color = TextPrimary
          )
          if (!goal.category.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = goal.category,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp
              ),
              color = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Goal Priority Badge
        val (goalPriorityText, goalPriorityColor, goalPriorityBg) = when (goal.priority) {
          Priority.HIGH -> Triple("عالية", PriorityHigh, Color(0xFFFEF2F2))
          Priority.MEDIUM -> Triple("متوسطة", Color(0xFFD97706), Color(0xFFFFFBEB))
          Priority.LOW -> Triple("منخفضة", PriorityLow, Color(0xFFF1F5F9))
          Priority.NONE -> Triple("بدون أولوية", TextSecondary, Color(0xFFF8FAFC))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(goalPriorityBg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = goalPriorityText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = goalPriorityColor
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress percentage badge
        Text(
          text = "${goal.progressPercentage}%",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = GoalBlue
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Expand/Collapse Chevron Indicator
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = if (isExpanded) "طي" else "توسيع",
          tint = TextSecondary,
          modifier = Modifier
            .size(20.dp)
            .rotate(rotationAngle)
        )
      }

      // 2. Expandable Body: Today's Tasks for this goal
      AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)) + fadeIn(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFC))
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          HorizontalDivider(
            color = BorderLight.copy(alpha = 0.5f),
            thickness = 0.8.dp,
            modifier = Modifier.padding(bottom = 6.dp)
          )

          val todayTasks = goal.todayTasks

          if (todayTasks.isEmpty()) {
            // Subtle empty state message when expanded
            Text(
              text = "لا توجد مهام لهذا الهدف اليوم",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
              ),
              color = TextMuted,
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
            )
          } else {
            // List of today's tasks belonging to this goal
            todayTasks.forEach { task ->
              GoalTaskAccordionRow(
                task = task,
                onToggle = { onToggleGoalTask(task.id) },
                onPostpone = if (onPostponeGoalTask != null) { { onPostponeGoalTask(task.id) } } else null
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun GoalTaskAccordionRow(
  task: Task,
  onToggle: () -> Unit,
  onPostpone: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) HabitEmerald else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) HabitEmerald else BorderLight,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onToggle)
      .padding(vertical = 7.dp, horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Checkbox circle
    Box(
      modifier = Modifier
        .size(20.dp)
        .clip(CircleShape)
        .background(checkBgColor)
        .border(1.5.dp, checkBorderColor, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      if (task.isCompleted) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "مكتملة",
          tint = TextWhite,
          modifier = Modifier.size(13.dp)
        )
      }
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Task title (with subtle completed state and strikethrough if completed)
    Text(
      text = task.title,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 13.sp,
        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
      ),
      color = if (task.isCompleted) TextSecondary.copy(alpha = 0.65f) else TextPrimary,
      modifier = Modifier.weight(1f)
    )

    // Postpone action if not completed, or postponed badge if already postponed
    if (!task.isCompleted && onPostpone != null) {
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFEFF6FF))
          .clickable { onPostpone() }
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = "ترحيل ➡️",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
          ),
          color = GoalBlue
        )
      }
    } else if (task.isPostponed) {
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFFFFBEB))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = "مُرحّلة ➡️",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          ),
          color = Color(0xFFD97706)
        )
      }
    }

    // Optional Priority tag if High
    if (task.priority == Priority.HIGH) {
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFFEF2F2))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = "عالية",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
          ),
          color = PriorityHigh
        )
      }
    }
  }
}
