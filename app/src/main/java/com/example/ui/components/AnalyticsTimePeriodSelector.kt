package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsTimePeriod
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextWhite

@Composable
fun AnalyticsTimePeriodSelector(
  selectedPeriod: AnalyticsTimePeriod,
  onSelectPeriod: (AnalyticsTimePeriod) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFFE2E8F0).copy(alpha = 0.5f))
      .padding(4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      AnalyticsTimePeriod.entries.forEach { period ->
        val isSelected = period == selectedPeriod
        val bgColor by animateColorAsState(
          targetValue = if (isSelected) SurfaceWhite else Color.Transparent,
          animationSpec = tween(durationMillis = 200),
          label = "periodBg"
        )
        val textColor by animateColorAsState(
          targetValue = if (isSelected) BrightBlue else TextPrimary,
          animationSpec = tween(durationMillis = 200),
          label = "periodText"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .then(
              if (isSelected) Modifier.shadow(2.dp, RoundedCornerShape(12.dp)) else Modifier
            )
            .background(bgColor)
            .clickable { onSelectPeriod(period) }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = period.iconText,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = period.titleArabic,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp
              ),
              color = textColor
            )
          }
        }
      }
    }
  }
}
