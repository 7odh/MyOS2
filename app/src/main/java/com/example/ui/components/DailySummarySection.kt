package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyAnalytics
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.extraColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailySummarySection(
  analytics: DailyAnalytics,
  onNavigateToAnalytics: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors

  // Formatted Arabic date matching mockup style: "الخميس ، 1 أكتوبر 2026"
  val formattedDate = try {
    val sdf = SimpleDateFormat("EEEE ، d MMMM yyyy", Locale("ar"))
    sdf.format(Date())
  } catch (e: Exception) {
    "الخميس ، 1 أكتوبر 2026"
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
  ) {
    // 1. Date and Period Selector Row (Directly below Welcome Card as in Mockup)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Calendar icon + Date
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Outlined.CalendarToday,
          contentDescription = "التاريخ",
          tint = extra.textSecondary,
          modifier = Modifier.size(17.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = formattedDate,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          ),
          color = extra.textSecondary
        )
      }

      // "أسبوعي" Pill Button
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(extra.secondarySurface)
          .border(1.dp, extra.border, RoundedCornerShape(12.dp))
          .clickable(onClick = onNavigateToAnalytics)
          .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.BarChart,
            contentDescription = "أسبوعي",
            tint = BrandPrimary,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "أسبوعي",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = BrandPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 2. Summary Header Row: "📊 ملخص يومك" and "التحليلات الشاملة 📊"
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Title with chart icon
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable(onClick = onNavigateToAnalytics)
          .padding(vertical = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.BarChart,
          contentDescription = "ملخص يومك",
          tint = BrandPrimary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ملخص يومك",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          ),
          color = extra.textPrimary
        )
      }

      // Full Analytics Pill Button
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(extra.secondarySurface)
          .border(1.dp, extra.border, RoundedCornerShape(12.dp))
          .clickable(onClick = onNavigateToAnalytics)
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.BarChart,
            contentDescription = "التحليلات الشاملة",
            tint = BrandPrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "التحليلات الشاملة",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            ),
            color = BrandPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 3. Three Metric Cards: Goals (right in RTL), Habits (center), Tasks (left)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Goals Card (Blue - #3B82F6)
      CircularProgressIndicatorCard(
        title = "الأهداف",
        icon = Icons.Outlined.Whatshot,
        percentage = analytics.goalsPercentage,
        completedCount = analytics.goalsCompleted,
        totalCount = analytics.goalsTotal,
        remainingCount = analytics.goalsRemaining,
        accentColor = GoalBlue,
        accentBgColor = extra.goalBg,
        trackColor = extra.goalTrack,
        modifier = Modifier.weight(1f)
      )

      // Habits Card (Emerald - #10B981)
      CircularProgressIndicatorCard(
        title = "العادات",
        icon = Icons.Outlined.Spa,
        percentage = analytics.habitsPercentage,
        completedCount = analytics.habitsCompleted,
        totalCount = analytics.habitsTotal,
        remainingCount = analytics.habitsRemaining,
        accentColor = HabitEmerald,
        accentBgColor = extra.habitBg,
        trackColor = extra.habitTrack,
        modifier = Modifier.weight(1f)
      )

      // Tasks Card (Violet - #8B5CF6)
      CircularProgressIndicatorCard(
        title = "المهام",
        icon = Icons.Outlined.CheckBox,
        percentage = analytics.tasksPercentage,
        completedCount = analytics.tasksCompleted,
        totalCount = analytics.tasksTotal,
        remainingCount = analytics.tasksRemaining,
        accentColor = TaskViolet,
        accentBgColor = extra.taskBg,
        trackColor = extra.taskTrack,
        modifier = Modifier.weight(1f)
      )
    }
  }
}
