package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.model.Priority
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.PriorityMediumBg
import com.example.ui.theme.TextSecondary

@Composable
fun PriorityBadge(
  priority: Priority,
  modifier: Modifier = Modifier
) {
  val (textColor, bgColor) = when (priority) {
    Priority.HIGH -> Pair(PriorityHigh, PriorityHighBg)
    Priority.MEDIUM -> Pair(PriorityMedium, PriorityMediumBg)
    Priority.LOW -> Pair(PriorityLow, PriorityLowBg)
    Priority.NONE -> Pair(PriorityLow, PriorityLowBg)
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = "الأولوية:",
      style = MaterialTheme.typography.labelSmall,
      color = TextSecondary
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = priority.titleArabic,
      style = MaterialTheme.typography.labelSmall,
      color = textColor
    )
  }
}
