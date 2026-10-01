package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsReport
import com.example.ui.theme.AnalyticsProgressEmerald
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun AnalyticsOverviewCard(
  report: AnalyticsReport,
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = report.productivityScore / 100f,
    animationSpec = tween(durationMillis = 900),
    label = "scoreProgress"
  )

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
      // Top Row: Title & Grade
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "مؤشر الإنتاجية والالتزام",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = TextPrimary
          )
          Text(
            text = report.period.descriptionArabic,
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp
            ),
            color = TextSecondary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              brush = Brush.linearGradient(
                listOf(BrightBlue, DeepBlue)
              )
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = report.productivityGrade,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = TextWhite
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Center Gauge / Score Display
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Circular Gauge
        Box(
          modifier = Modifier.size(105.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.size(105.dp)) {
            val strokeWidth = 10.dp.toPx()

            // Background Track
            drawArc(
              color = Color(0xFFF1F5F9),
              startAngle = -90f,
              sweepAngle = 360f,
              useCenter = false,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Animated Gradient Arc
            drawArc(
              brush = Brush.sweepGradient(
                colors = listOf(
                  AnalyticsProgressEmerald,
                  BrightBlue,
                  ElectricViolet,
                  AnalyticsProgressEmerald
                )
              ),
              startAngle = -90f,
              sweepAngle = animatedProgress * 360f,
              useCenter = false,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
          }

          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "${report.productivityScore}%",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp
              ),
              color = TextPrimary
            )
            Text(
              text = "نقاط الأداء",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              ),
              color = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Key Metric Summary Grid (4 mini tiles)
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            MetricTile(
              label = "المكتمل",
              value = "${report.totalCompletedItems}",
              color = AnalyticsProgressEmerald,
              bgColor = Color(0xFFECFDF5),
              modifier = Modifier.weight(1f)
            )
            MetricTile(
              label = "المخطط",
              value = "${report.totalPlannedItems}",
              color = BrightBlue,
              bgColor = Color(0xFFEFF6FF),
              modifier = Modifier.weight(1f)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            MetricTile(
              label = "نسبة الإنجاز",
              value = "${report.overallCompletionRate}%",
              color = ElectricViolet,
              bgColor = Color(0xFFF5F3FF),
              modifier = Modifier.weight(1f)
            )
            MetricTile(
              label = "دقائق التركيز",
              value = "${report.focusTotalMinutes}د",
              color = Color(0xFF0284C7),
              bgColor = Color(0xFFE0F2FE),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(color = BorderLight.copy(alpha = 0.6f), thickness = 1.dp)
      Spacer(modifier = Modifier.height(12.dp))

      // Motivational Quote
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "💬",
          fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = report.motivationalQuote,
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 17.sp
          ),
          color = TextSecondary,
          textAlign = TextAlign.Start
        )
      }
    }
  }
}

@Composable
private fun MetricTile(
  label: String,
  value: String,
  color: Color,
  bgColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 10.dp, vertical = 8.dp)
  ) {
    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        ),
        color = TextSecondary
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        ),
        color = color
      )
    }
  }
}
