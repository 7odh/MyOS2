package com.example.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InsightType
import com.example.model.ProductivityInsight
import com.example.ui.theme.AnalyticsProgressEmerald
import com.example.ui.theme.AnalyticsRestAmber
import com.example.ui.theme.AnalyticsShortfallRed
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AnalyticsSmartInsightsSection(
  insights: List<ProductivityInsight>,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "الرؤى والتحليلات الذكية 🧠",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "توصيات وملاحظات مخصصة لتحسين روتينك",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = TextSecondary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFEFF6FF))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "توجيهات MyOS",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = BrightBlue
          )
        }
      }

      Spacer(modifier = Modifier.height(2.dp))

      insights.forEach { insight ->
        val (accentColor, bgColor) = when (insight.type) {
          InsightType.SUCCESS -> Pair(AnalyticsProgressEmerald, Color(0xFFECFDF5))
          InsightType.WARNING -> Pair(AnalyticsShortfallRed, Color(0xFFFEF2F2))
          InsightType.REST -> Pair(AnalyticsRestAmber, Color(0xFFFFFBEB))
          InsightType.INFO -> Pair(BrightBlue, Color(0xFFEFF6FF))
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = insight.iconEmoji,
            fontSize = 22.sp
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = insight.title,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              ),
              color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = insight.description,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 17.sp
              ),
              color = TextPrimary
            )
          }
        }
      }
    }
  }
}
