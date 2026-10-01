package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TaskItemRow(
  task: Task,
  onToggle: () -> Unit,
  onPostpone: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else BorderLight,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  val (priorityText, priorityTextColor, priorityBg) = when (task.priority) {
    Priority.HIGH -> Triple("عالية", PriorityHigh, Color(0xFFFEF2F2))
    Priority.MEDIUM -> Triple("متوسطة", Color(0xFFD97706), Color(0xFFFFFBEB))
    Priority.LOW -> Triple("منخفضة", PriorityLow, Color(0xFFF1F5F9))
    Priority.NONE -> Triple("بدون أولوية", TextSecondary, Color(0xFFF8FAFC))
  }

  // Swipe / Drag to postpone state (TickTick style gesture)
  val offsetX = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()
  val density = LocalDensity.current
  val swipeThresholdPx = with(density) { 72.dp.toPx() }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
  ) {
    // Revealed background when swiping (TickTick Postpone Indicator)
    if (onPostpone != null && !task.isCompleted) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFFEFF6FF)),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = "ترحيل للغد",
            tint = BrightBlue,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "تمرير للترحيل للغد ➡️",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = BrightBlue
          )
        }
      }
    }

    // Foreground draggable Task item
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surface)
        .clickable(onClick = onToggle)
        .then(
          if (onPostpone != null && !task.isCompleted) {
            Modifier.pointerInput(task.id) {
              detectHorizontalDragGestures(
                onDragEnd = {
                  scope.launch {
                    if (abs(offsetX.value) >= swipeThresholdPx) {
                      onPostpone()
                    }
                    offsetX.animateTo(0f, animationSpec = spring())
                  }
                },
                onDragCancel = {
                  scope.launch { offsetX.animateTo(0f, animationSpec = spring()) }
                },
                onHorizontalDrag = { _, dragAmount ->
                  scope.launch {
                    val nextOffset = (offsetX.value + dragAmount).coerceIn(-swipeThresholdPx * 1.5f, swipeThresholdPx * 1.5f)
                    offsetX.snapTo(nextOffset)
                  }
                }
              )
            }
          } else Modifier
        )
        .padding(horizontal = 4.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Custom Check Circle
      Box(
        modifier = Modifier
          .size(24.dp)
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
            modifier = Modifier.size(15.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Task Title
      Text(
        text = task.title,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontSize = 14.sp,
          fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
          textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
        ),
        color = if (task.isCompleted) TextSecondary.copy(alpha = 0.65f) else TextPrimary,
        modifier = Modifier.weight(1f)
      )

      Spacer(modifier = Modifier.width(8.dp))

      // Postpone button or badge
      if (!task.isCompleted && onPostpone != null) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEFF6FF))
            .clickable { onPostpone() }
            .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
          Text(
            text = "ترحيل للغد ➡️",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            ),
            color = BrightBlue
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
      } else if (task.isPostponed) {
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
        Spacer(modifier = Modifier.width(6.dp))
      }

      // Task's individual Priority Badge (Only when not None)
      if (task.priority != Priority.NONE) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(priorityBg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = priorityText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            ),
            color = priorityTextColor
          )
        }
      }
    }
  }
}
