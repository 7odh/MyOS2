package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
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
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.GoalBlueTrack
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TaskVioletTrack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailySummarySection(
  analytics: DailyAnalytics,
  onNavigateToAnalytics: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  // Format current Arabic date or match reference "السبت، 27 سبتمبر 2025"
  val formattedDate = try {
    val sdf = SimpleDateFormat("EEEE، d MMMM yyyy", Locale("ar"))
    sdf.format(Date())
  } catch (e: Exception) {
    "السبت، 27 سبتمبر 2025"
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Header Row: "ملخص يومك" and Navigation to Analytics
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Title with chart icon (clickable)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable(onClick = onNavigateToAnalytics)
          .padding(vertical = 4.dp, horizontal = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.BarChart,
          contentDescription = "ملخص يومك",
          tint = BrightBlue,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ملخص يومك",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          ),
          color = TextPrimary
        )
      }

      // Analytics badge button
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(GoalBlueBg)
          .clickable(onClick = onNavigateToAnalytics)
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text(
          text = "التحليلات الشاملة 📊",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          ),
          color = BrightBlue
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 3 Circular Progress Cards (Conditional: hide empty categories)
    val hasGoals = analytics.goalsTotal > 0
    val hasHabits = analytics.habitsTotal > 0
    val hasTasks = analytics.tasksTotal > 0

    if (hasGoals || hasHabits || hasTasks) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Goals Card (Blue)
        if (hasGoals) {
          CircularProgressIndicatorCard(
            title = "الأهداف",
            icon = Icons.Outlined.TrackChanges,
            percentage = analytics.goalsPercentage,
            completedCount = analytics.goalsCompleted,
            totalCount = analytics.goalsTotal,
            remainingCount = analytics.goalsRemaining,
            accentColor = GoalBlue,
            accentBgColor = GoalBlueBg,
            trackColor = GoalBlueTrack,
            modifier = Modifier.weight(1f)
          )
        }

        // Habits Card (Emerald)
        if (hasHabits) {
          CircularProgressIndicatorCard(
            title = "العادات",
            icon = Icons.Outlined.Spa,
            percentage = analytics.habitsPercentage,
            completedCount = analytics.habitsCompleted,
            totalCount = analytics.habitsTotal,
            remainingCount = analytics.habitsRemaining,
            accentColor = HabitEmerald,
            accentBgColor = HabitEmeraldBg,
            trackColor = HabitEmeraldTrack,
            modifier = Modifier.weight(1f)
          )
        }

        // Tasks Card (Purple/Violet)
        if (hasTasks) {
          CircularProgressIndicatorCard(
            title = "المهام",
            icon = Icons.Outlined.CheckBox,
            percentage = analytics.tasksPercentage,
            completedCount = analytics.tasksCompleted,
            totalCount = analytics.tasksTotal,
            remainingCount = analytics.tasksRemaining,
            accentColor = TaskViolet,
            accentBgColor = TaskVioletBg,
            trackColor = TaskVioletTrack,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
