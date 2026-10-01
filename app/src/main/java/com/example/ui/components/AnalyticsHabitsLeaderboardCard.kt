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
import com.example.model.Habit
import com.example.ui.theme.AnalyticsProgressEmerald
import com.example.ui.theme.AnalyticsRestAmber
import com.example.ui.theme.BorderLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AnalyticsHabitsLeaderboardCard(
  habits: List<Habit>,
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
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "لوحة شرف العادات وسلسلة الأيام 🔥",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "ترتيب العادات بحسب أطول استمرارية (Streak)",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = TextSecondary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF3C7))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "🏆 الترتيب الحي",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = Color(0xFFB45309)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      habits.take(5).forEachIndexed { index, habit ->
        val rankBadge = when (index) {
          0 -> "🥇"
          1 -> "🥈"
          2 -> "🥉"
          else -> "${index + 1}"
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Text(rankBadge, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(habit.iconEmoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))

            Column {
              Text(
                text = habit.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                if (habit.isMandatory) {
                  Text(
                    text = "إجبارية 🛡️ • ",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color(0xFF6366F1)
                  )
                }
                if (habit.isCurrentlyPaused) {
                  Text(
                    text = "مركونة مؤقتاً ⏸️",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = AnalyticsRestAmber
                  )
                } else {
                  Text(
                    text = "نشطة ومستمرة",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = AnalyticsProgressEmerald
                  )
                }
              }
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFFFF7ED))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "${habit.currentStreak} يوم 🔥",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = Color(0xFFEA580C)
            )
          }
        }
      }
    }
  }
}
