package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.Task
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.extraColors

@Composable
fun TodaySection(
  habits: List<Habit>,
  tasks: List<Task>,
  goals: List<Goal>,
  isRestModeActive: Boolean = false,
  onIncrementHabit: (String) -> Unit,
  onOpenLogHabitDialog: (Habit) -> Unit,
  onToggleHabitBoolean: (String) -> Unit,
  onToggleTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit = {},
  onPostponeGoalTask: (String, String) -> Unit = { _, _ -> },
  onViewAllClick: () -> Unit,
  onNavigateToTasks: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val isDark = extra.isDark

  val todayDay = com.example.model.getCurrentDayOfWeekArabic()
  val todayScheduledHabits = habits.filter { it.isScheduledForToday(todayDay) }
  val displayHabits = if (isRestModeActive) {
    todayScheduledHabits.filter { it.isMandatory }
  } else {
    todayScheduledHabits
  }

  val activeGoals = goals.filter { !it.isPaused && it.progressPercentage < 100 && it.todayTasks.isNotEmpty() }

  var expandedGoalIds by remember(activeGoals.map { it.id }) {
    mutableStateOf(setOfNotNull(activeGoals.firstOrNull()?.id))
  }

  val generalTasks = tasks.filter { !it.isGoalTask && it.isDueToday }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    // Section Header: "📋 عليك اليوم" and "عرض الكل"
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Outlined.List,
          contentDescription = "عليك اليوم",
          tint = BrandPrimary,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "عليك اليوم",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = extra.textPrimary
        )
      }

      TextButton(
        onClick = onViewAllClick,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = "عرض الكل",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = BrandPrimary
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 1. Habits Sub-Section Card
    if (displayHabits.isNotEmpty()) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = if (isDark) 0.dp else 3.dp,
            shape = RoundedCornerShape(22.dp),
            ambientColor = extra.shadow,
            spotColor = extra.shadow
          )
          .clip(RoundedCornerShape(22.dp))
          .border(1.dp, extra.border, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = extra.cardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(extra.cardSurface)
            .padding(14.dp)
        ) {
          // Habits Sub-Header Row matching mockup: "العادات 🌱" on right, "X من Y مكتملة" on left
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(extra.habitBg),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.Spa,
                  contentDescription = "العادات",
                  tint = HabitEmerald,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "العادات",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                ),
                color = HabitEmerald
              )
            }

            Text(
              text = "${displayHabits.count { it.isCompleted }} من ${displayHabits.size} مكتملة",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = HabitEmerald
            )
          }

          if (isRestModeActive) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isDark) Color(0xFF451A03).copy(alpha = 0.6f) else Color(0xFFFEF3C7))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "وضع الراحة مفعّل: تظهر العادات الإجبارية فقط، وبقية العادات في استراحة مستحقة ☕",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                color = if (isDark) Color(0xFFFDE68A) else Color(0xFFB45309)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Habits list
          displayHabits.forEach { habit ->
            HabitItemRow(
              habit = habit,
              onIncrementCounter = { onIncrementHabit(habit.id) },
              onOpenLogDialog = { onOpenLogHabitDialog(habit) },
              onToggleBoolean = { onToggleHabitBoolean(habit.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    // 2. General Tasks Sub-Section Card
    if (generalTasks.isNotEmpty()) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = if (isDark) 0.dp else 3.dp,
            shape = RoundedCornerShape(22.dp),
            ambientColor = extra.shadow,
            spotColor = extra.shadow
          )
          .clip(RoundedCornerShape(22.dp))
          .border(1.dp, extra.border, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = extra.cardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(extra.cardSurface)
            .padding(14.dp)
        ) {
          // Tasks Header Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onNavigateToTasks() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(extra.taskBg),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.CheckBox,
                  contentDescription = "المهام",
                  tint = TaskViolet,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "المهام المستحقة اليوم",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                ),
                color = TaskViolet
              )
            }

            Text(
              text = "${generalTasks.count { it.isCompleted }} من ${generalTasks.size} مكتملة  ←",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = TaskViolet
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          val displayTasks = generalTasks
            .sortedWith(
              compareBy<Task> { it.isCompleted }
                .thenBy { it.priority.rank }
                .thenBy { it.createdAt }
            )
            .take(5)
          displayTasks.forEach { task ->
            TaskItemRow(
              task = task,
              onToggle = { onToggleTask(task.id) },
              onPostpone = { onPostponeTask(task.id) }
            )
            Spacer(modifier = Modifier.height(6.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    // 3. Goals Sub-Section Card
    if (activeGoals.isNotEmpty()) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = if (isDark) 0.dp else 3.dp,
            shape = RoundedCornerShape(22.dp),
            ambientColor = extra.shadow,
            spotColor = extra.shadow
          )
          .clip(RoundedCornerShape(22.dp))
          .border(1.dp, extra.border, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = extra.cardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(extra.cardSurface)
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(extra.goalBg),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.Whatshot,
                  contentDescription = "الأهداف",
                  tint = GoalBlue,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "الأهداف",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                ),
                color = GoalBlue
              )
            }

            Text(
              text = "${activeGoals.size} أهداف نشطة",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = GoalBlue
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          activeGoals.forEach { goal ->
            val isExpanded = expandedGoalIds.contains(goal.id)
            GoalAccordionItem(
              goal = goal,
              isExpanded = isExpanded,
              onToggleExpand = {
                expandedGoalIds = if (isExpanded) {
                  expandedGoalIds - goal.id
                } else {
                  expandedGoalIds + goal.id
                }
              },
              onToggleGoalTask = { taskId ->
                onToggleGoalTask(goal.id, taskId)
              },
              onPostponeGoalTask = { taskId ->
                onPostponeGoalTask(goal.id, taskId)
              }
            )
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }
  }
}
