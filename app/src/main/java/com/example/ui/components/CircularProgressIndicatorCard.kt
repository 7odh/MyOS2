package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CircularProgressIndicatorCard(
  title: String,
  icon: ImageVector,
  percentage: Int,
  completedCount: Int,
  totalCount: Int,
  remainingCount: Int,
  accentColor: Color,
  accentBgColor: Color,
  trackColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top icon badge
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(accentBgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Category Title
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        ),
        color = accentColor
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Circular Ring with Animated Progress
      CircularProgressRing(
        percentage = percentage,
        strokeColor = accentColor,
        trackColor = trackColor,
        ringSize = 64.dp,
        strokeWidth = 6.dp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Completed / Total summary text
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "$completedCount / $totalCount",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = TextPrimary
        )
      }
      Text(
        text = "مكتملة",
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 10.sp
        ),
        color = TextSecondary
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Remaining count
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "متبقية ",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp
          ),
          color = TextSecondary
        )
        Text(
          text = "$remainingCount",
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          ),
          color = TextPrimary
        )
      }
    }
  }
}

@Composable
fun CircularProgressRing(
  percentage: Int,
  strokeColor: Color,
  trackColor: Color,
  ringSize: Dp = 64.dp,
  strokeWidth: Dp = 6.dp,
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = (percentage.coerceIn(0, 100) / 100f),
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
    label = "progressAnimation"
  )

  Box(
    modifier = modifier.size(ringSize),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(ringSize)) {
      val strokePx = strokeWidth.toPx()
      val arcSize = Size(size.width - strokePx, size.height - strokePx)
      val topLeft = Offset(strokePx / 2f, strokePx / 2f)

      // Draw background track
      drawArc(
        color = trackColor,
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokePx, cap = StrokeCap.Round)
      )

      // Draw animated progress arc
      val sweep = animatedProgress * 360f
      if (sweep > 0f) {
        drawArc(
          color = strokeColor,
          startAngle = -90f,
          sweepAngle = sweep,
          useCenter = false,
          topLeft = topLeft,
          size = arcSize,
          style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
      }
    }

    Text(
      text = "$percentage%",
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      ),
      color = TextPrimary
    )
  }
}
