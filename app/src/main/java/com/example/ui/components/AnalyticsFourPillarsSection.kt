package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsReport
import com.example.model.MissedHabitStat
import com.example.model.PostponedTaskStat
import com.example.ui.theme.AnalyticsFocusViolet
import com.example.ui.theme.AnalyticsFocusVioletBg
import com.example.ui.theme.AnalyticsPostponeSky
import com.example.ui.theme.AnalyticsPostponeSkyBg
import com.example.ui.theme.AnalyticsProgressEmerald
import com.example.ui.theme.AnalyticsProgressEmeraldBg
import com.example.ui.theme.AnalyticsRestAmber
import com.example.ui.theme.AnalyticsRestAmberBg
import com.example.ui.theme.AnalyticsShortfallRed
import com.example.ui.theme.AnalyticsShortfallRedBg
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun AnalyticsFourPillarsSection(
  report: AnalyticsReport,
  modifier: Modifier = Modifier
) {
  var isProgressExpanded by remember { mutableStateOf(true) }
  var isShortfallExpanded by remember { mutableStateOf(true) }
  var isRestExpanded by remember { mutableStateOf(false) }
  var isPostponedExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "الأركان التحليلية الأربعة",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        ),
        color = TextPrimary
      )
      Text(
        text = "مراجعة شاملة ومفصلة",
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 12.sp
        ),
        color = TextSecondary
      )
    }

    // 1. التقدم (Progress Pillar)
    PillarCard(
      title = "التقدم والإنجاز 🚀",
      subtitle = "نسبة تحقيق المطلوب: ${report.overallCompletionRate}%",
      mainValue = "${report.totalCompletedItems} من ${report.totalPlannedItems}",
      accentColor = AnalyticsProgressEmerald,
      accentBg = AnalyticsProgressEmeraldBg,
      icon = Icons.Outlined.CheckCircle,
      isExpanded = isProgressExpanded,
      onToggleExpand = { isProgressExpanded = !isProgressExpanded }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Progress Bars for each category
        ProgressRow(
          title = "العادات اليومية",
          completed = report.habitsCompleted,
          total = report.habitsTotal,
          percentage = report.habitsRate,
          barColor = AnalyticsProgressEmerald,
          unit = "عادة"
        )
        ProgressRow(
          title = "المهام العامة",
          completed = report.tasksCompleted,
          total = report.tasksTotal,
          percentage = report.tasksRate,
          barColor = BrightBlue,
          unit = "مهمة"
        )
        ProgressRow(
          title = "مهام الأهداف الكبرى",
          completed = report.goalTasksCompleted,
          total = report.goalTasksTotal,
          percentage = report.goalTasksRate,
          barColor = Color(0xFF6366F1),
          unit = "خطوة"
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = BorderLight.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(4.dp))

        // Focus sessions highlight
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AnalyticsFocusVioletBg)
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("⏱️", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "جلسات التركيز وبومودورو",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
              )
              Text(
                text = "${report.focusSessionsCount} جلسات عمل عميق مكتملة",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }
          }

          Text(
            text = "${report.focusTotalMinutes} دقيقة",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = AnalyticsFocusViolet
          )
        }
      }
    }

    // 2. التقصير والفجوات (Shortfall Pillar)
    PillarCard(
      title = "التقصير وما فاتك ⚠️",
      subtitle = "نسبة الفجوة والتقصير: ${report.missedRate}%",
      mainValue = "${report.totalMissedItems} عناصر",
      accentColor = AnalyticsShortfallRed,
      accentBg = AnalyticsShortfallRedBg,
      icon = Icons.Outlined.ErrorOutline,
      isExpanded = isShortfallExpanded,
      onToggleExpand = { isShortfallExpanded = !isShortfallExpanded }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (report.totalMissedItems == 0) {
          Text(
            text = "🌟 تهانينا! لا يوجد أي تقصير مسجل في هذه الفترة، أداء مثالي وانضباط تام.",
            style = MaterialTheme.typography.bodyMedium,
            color = AnalyticsProgressEmerald,
            modifier = Modifier.padding(vertical = 4.dp)
          )
        } else {
          Text(
            text = "العادات والأنشطة الأكثر تأثراً بالتقصير:",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = TextSecondary
          )

          report.topMissedHabits.forEach { missed ->
            MissedHabitItemRow(missed = missed)
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Practical constructive advice
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFFEF2F2))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.Top) {
              Text("💡", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "نصيحة لمعالجة التقصير:",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = AnalyticsShortfallRed
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = report.shortfallAnalysisAdvice,
                  style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                  color = TextPrimary
                )
              }
            }
          }
        }
      }
    }

    // 3. الراحة والاستشفاء وتجميد العادات (Rest Pillar)
    PillarCard(
      title = "الراحة وتجميد العادات ☕",
      subtitle = report.restBalanceStatus,
      mainValue = "${report.restDaysCount} أيام راحة",
      accentColor = AnalyticsRestAmber,
      accentBg = AnalyticsRestAmberBg,
      icon = Icons.Outlined.Spa,
      isExpanded = isRestExpanded,
      onToggleExpand = { isRestExpanded = !isRestExpanded }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          RestStatCard(
            title = "أيام وضع الراحة ☕",
            value = "${report.restDaysCount}",
            subtitle = "أيام مستحقة",
            modifier = Modifier.weight(1f)
          )
          RestStatCard(
            title = "العادات المركونة ⏸️",
            value = "${report.pausedHabitsCount}",
            subtitle = "مجمدة لحفظ الستريك",
            modifier = Modifier.weight(1f)
          )
        }

        // Philosophy note
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AnalyticsRestAmberBg)
            .padding(12.dp)
        ) {
          Row(verticalAlignment = Alignment.Top) {
            Text("🛡️", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "قاعدة حماية الـ Streaks عند الراحة:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFB45309)
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "في MyOS، أيام الراحة وتجميد العادات لا تُسجل كفشل أو كسر للستريك، بل كإجراء استراتيجي لحماية طاقتك وضمان عودتك القوية.",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                color = TextPrimary
              )
            }
          }
        }
      }
    }

    // 4. التأجيل والترحيل لليوم التالي (Postponed Pillar)
    PillarCard(
      title = "التأجيل والترحيل للغد ➡️",
      subtitle = report.postponementTrendNote,
      mainValue = "${report.postponedTasksCount} مهام",
      accentColor = AnalyticsPostponeSky,
      accentBg = AnalyticsPostponeSkyBg,
      icon = Icons.Outlined.Schedule,
      isExpanded = isPostponedExpanded,
      onToggleExpand = { isPostponedExpanded = !isPostponedExpanded }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (report.topPostponedTasks.isEmpty()) {
          Text(
            text = "🎯 رائع جداً! لا توجد مهام مؤجلة أو مرحّلة في هذه الفترة، تخطيط وإنجاز فوري ممتاز.",
            style = MaterialTheme.typography.bodyMedium,
            color = AnalyticsProgressEmerald,
            modifier = Modifier.padding(vertical = 4.dp)
          )
        } else {
          Text(
            text = "المهام الأكثر ترحيلاً وتأجيلاً:",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = TextSecondary
          )

          report.topPostponedTasks.forEach { task ->
            PostponedTaskRow(task = task)
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Advice on postponement
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(AnalyticsPostponeSkyBg)
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.Top) {
              Text("📌", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "إدارة الترحيل والتأجيل الذكي:",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = AnalyticsPostponeSky
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "تمرير المهمة وترحيلها لليوم التالي ميزة ذكية لتخفيف الضغط؛ لكن إذا تكرر تأجيل نفس المهمة أكثر من 3 مرات، قسّمها لخطوات أصغر أو قلل أولويتها.",
                  style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                  color = TextPrimary
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PillarCard(
  title: String,
  subtitle: String,
  mainValue: String,
  accentColor: Color,
  accentBg: Color,
  icon: ImageVector,
  isExpanded: Boolean,
  onToggleExpand: () -> Unit,
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header Row (Clickable to toggle expand)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .clickable(onClick = onToggleExpand)
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(accentBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = title,
              tint = accentColor,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              ),
              color = TextPrimary
            )
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = TextSecondary
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(accentBg)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = mainValue,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              ),
              color = accentColor
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "طي" else "توسيع",
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
        ) {
          HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), thickness = 1.dp)
          Spacer(modifier = Modifier.height(12.dp))
          content()
        }
      }
    }
  }
}

@Composable
private fun ProgressRow(
  title: String,
  completed: Int,
  total: Int,
  percentage: Int,
  barColor: Color,
  unit: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
        color = TextPrimary
      )
      Text(
        text = "$completed من $total $unit ($percentage%)",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        ),
        color = barColor
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    LinearProgressIndicator(
      progress = { if (total > 0) (completed.toFloat() / total).coerceIn(0f, 1f) else 0f },
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp)),
      color = barColor,
      trackColor = Color(0xFFF1F5F9),
      strokeCap = StrokeCap.Round
    )
  }
}

@Composable
private fun MissedHabitItemRow(missed: MissedHabitStat) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFFFEF2F2).copy(alpha = 0.6f))
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Text(missed.iconEmoji, fontSize = 20.sp)
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = missed.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Text(
          text = missed.reasonNote,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = TextSecondary
        )
      }
    }

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(AnalyticsShortfallRed.copy(alpha = 0.15f))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(
        text = "${missed.missedCount} مرات تقصير",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        ),
        color = AnalyticsShortfallRed
      )
    }
  }
}

@Composable
private fun RestStatCard(
  title: String,
  value: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFFFFBEB))
      .padding(12.dp)
  ) {
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = TextPrimary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.ExtraBold,
          fontSize = 20.sp
        ),
        color = AnalyticsRestAmber
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = TextSecondary
      )
    }
  }
}

@Composable
private fun PostponedTaskRow(task: PostponedTaskStat) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFFE0F2FE).copy(alpha = 0.5f))
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Text("➡️", fontSize = 18.sp)
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        task.originalDate?.let {
          Text(
            text = "رُحّلت من: $it",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondary
          )
        }
      }
    }

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(AnalyticsPostponeSky.copy(alpha = 0.15f))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(
        text = "مُرَحّلة ${task.postponedCount}x",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        ),
        color = AnalyticsPostponeSky
      )
    }
  }
}
