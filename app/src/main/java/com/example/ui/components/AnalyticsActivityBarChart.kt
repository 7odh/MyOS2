package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsBarData
import com.example.ui.theme.AnalyticsPostponeSky
import com.example.ui.theme.AnalyticsProgressEmerald
import com.example.ui.theme.AnalyticsShortfallRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AnalyticsActivityBarChart(
  bars: List<AnalyticsBarData>,
  periodTitle: String,
  modifier: Modifier = Modifier
) {
  var selectedBarIndex by remember { mutableStateOf<Int?>(null) }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // Header & Legend
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "مخطط الأنشطة والإنجاز 📊",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            ),
            color = TextPrimary
          )
          Text(
            text = periodTitle,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = TextSecondary
          )
        }

        // Legend
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          LegendDot(color = AnalyticsProgressEmerald, label = "منجز")
          LegendDot(color = AnalyticsPostponeSky, label = "مُرَحّل")
          LegendDot(color = AnalyticsShortfallRed, label = "تقصير")
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Canvas Interactive Chart
      val maxTotal = (bars.maxOfOrNull { it.totalCount } ?: 10).coerceAtLeast(6)

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Canvas(
          modifier = Modifier
            .matchParentSize()
            .pointerInput(bars) {
              detectTapGestures { offset ->
                val barWidthWithSpacing = size.width / bars.size
                val index = (offset.x / barWidthWithSpacing).toInt().coerceIn(0, bars.size - 1)
                selectedBarIndex = if (selectedBarIndex == index) null else index
              }
            }
        ) {
          val canvasWidth = size.width
          val canvasHeight = size.height - 24.dp.toPx() // leave bottom room for labels
          val barGroupWidth = canvasWidth / bars.size
          val barWidth = (barGroupWidth * 0.55f).coerceAtMost(36.dp.toPx())

          // Horizontal grid lines
          val gridLineCount = 4
          for (g in 0..gridLineCount) {
            val y = canvasHeight * (g.toFloat() / gridLineCount)
            drawLine(
              color = Color(0xFFF1F5F9),
              start = Offset(0f, y),
              end = Offset(canvasWidth, y),
              strokeWidth = 1.dp.toPx()
            )
          }

          bars.forEachIndexed { i, bar ->
            val centerX = (i * barGroupWidth) + (barGroupWidth / 2f)
            val barLeft = centerX - (barWidth / 2f)

            // Scaled heights
            val doneRatio = (bar.completedCount.toFloat() / maxTotal).coerceIn(0f, 1f)
            val postRatio = (bar.postponedCount.toFloat() / maxTotal).coerceIn(0f, 1f)
            val missRatio = (bar.missedCount.toFloat() / maxTotal).coerceIn(0f, 1f)

            val doneHeight = canvasHeight * doneRatio
            val postHeight = canvasHeight * postRatio
            val missHeight = canvasHeight * missRatio

            val isSelected = selectedBarIndex == i

            // Draw Background Bar Track
            drawRoundRect(
              color = if (isSelected) Color(0xFFE2E8F0) else Color(0xFFF8FAFC),
              topLeft = Offset(barLeft, 0f),
              size = Size(barWidth, canvasHeight),
              cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            // Stacked drawing from bottom up
            var currentY = canvasHeight

            // 1. Completed (Bottom - Emerald)
            if (doneHeight > 0) {
              currentY -= doneHeight
              drawRoundRect(
                color = AnalyticsProgressEmerald,
                topLeft = Offset(barLeft, currentY),
                size = Size(barWidth, doneHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
              )
            }

            // 2. Postponed (Middle - Sky)
            if (postHeight > 0) {
              currentY -= postHeight
              drawRoundRect(
                color = AnalyticsPostponeSky,
                topLeft = Offset(barLeft, currentY),
                size = Size(barWidth, postHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
              )
            }

            // 3. Missed (Top - Red)
            if (missHeight > 0) {
              currentY -= missHeight
              drawRoundRect(
                color = AnalyticsShortfallRed,
                topLeft = Offset(barLeft, currentY),
                size = Size(barWidth, missHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
              )
            }
          }
        }
      }

      // X-Axis Labels Row below Canvas
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        bars.forEachIndexed { i, bar ->
          val isSelected = selectedBarIndex == i
          Text(
            text = bar.label,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (isSelected) BrightBlue else TextSecondary,
            modifier = Modifier.clickable {
              selectedBarIndex = if (selectedBarIndex == i) null else i
            }
          )
        }
      }

      // Selected Bar Details Popover / Banner
      selectedBarIndex?.let { idx ->
        val bar = bars.getOrNull(idx)
        if (bar != null) {
          Spacer(modifier = Modifier.height(14.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFEFF6FF))
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "تفاصيل ${bar.label}:",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = BrightBlue
                )
                Text(
                  text = "نسبة الإنجاز: ${bar.completionRate}%",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextPrimary
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BadgeText(text = "✅ ${bar.completedCount} منجز", color = AnalyticsProgressEmerald)
                if (bar.postponedCount > 0) {
                  BadgeText(text = "➡️ ${bar.postponedCount} مرحّل", color = AnalyticsPostponeSky)
                }
                if (bar.missedCount > 0) {
                  BadgeText(text = "❌ ${bar.missedCount} تقصير", color = AnalyticsShortfallRed)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LegendDot(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      color = TextSecondary
    )
  }
}

@Composable
private fun BadgeText(text: String, color: Color) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(color.copy(alpha = 0.15f))
      .padding(horizontal = 6.dp, vertical = 3.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      ),
      color = color
    )
  }
}
