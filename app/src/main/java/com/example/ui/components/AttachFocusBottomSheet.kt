package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FocusAttachType
import com.example.model.FocusAttachment
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.HabitType
import com.example.model.Task
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachFocusBottomSheet(
  currentAttachment: FocusAttachment,
  habits: List<Habit>,
  tasks: List<Task>,
  goals: List<Goal>,
  onSelectAttachment: (FocusAttachment) -> Unit,
  onClearAttachment: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var habitsExpanded by remember { mutableStateOf(true) }
  var tasksExpanded by remember { mutableStateOf(false) }
  var goalsExpanded by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = SurfaceWhite,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ربط جلسة التركيز 🔗",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "اختر عادة أو مهمة أو هدف لتسجيل وقت التركيز عليه تلقائياً",
            fontSize = 13.sp,
            color = TextSecondary
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "إغلاق",
            tint = TextMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f, fill = false),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Free Focus Session Option
        item {
          val isFreeSelected = currentAttachment.type == FocusAttachType.NONE
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                onClearAttachment()
                onDismiss()
              },
            color = if (isFreeSelected) GoalBlueBg else Color(0xFFF8FAFC),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isFreeSelected) BrightBlue else BorderLight
            )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(if (isFreeSelected) BrightBlue else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "🎯",
                  fontSize = 20.sp
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "جلسة تركيز حرة (بدون ربط)",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isFreeSelected) BrightBlue else TextPrimary
                )
                Text(
                  text = "التركيز بدون توجيه الوقت إلى نشاط محدد",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
              }
              if (isFreeSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "محدد",
                  tint = BrightBlue,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }

        // Section 1: Habits (Accordion)
        item {
          CollapsibleSectionCard(
            title = "العادات اليومية",
            emoji = "🌱",
            count = habits.size,
            isExpanded = habitsExpanded,
            accentColor = HabitEmerald,
            onToggleExpand = { habitsExpanded = !habitsExpanded }
          ) {
            Column(
              modifier = Modifier.padding(top = 8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              if (habits.isEmpty()) {
                Text(
                  text = "لا توجد عادات مسجلة حالياً",
                  fontSize = 13.sp,
                  color = TextMuted,
                  modifier = Modifier.padding(8.dp)
                )
              } else {
                habits.forEach { habit ->
                  val isSelected = currentAttachment.type == FocusAttachType.HABIT &&
                    currentAttachment.id == habit.id
                  val isDurationHabit = habit.type == HabitType.DURATION

                  Surface(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .clickable {
                        onSelectAttachment(
                          FocusAttachment(
                            type = FocusAttachType.HABIT,
                            id = habit.id,
                            title = habit.title,
                            iconEmoji = habit.iconEmoji,
                            targetMinutes = habit.targetValue,
                            currentMinutes = habit.currentValue,
                            unit = habit.unit,
                            isTimedHabit = isDurationHabit
                          )
                        )
                        onDismiss()
                      },
                    color = if (isSelected) HabitEmeraldBg else SurfaceWhite,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                      1.dp,
                      if (isSelected) HabitEmerald else BorderLight
                    )
                  ) {
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = habit.iconEmoji,
                        fontSize = 22.sp
                      )
                      Spacer(modifier = Modifier.width(10.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                            text = habit.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                          )
                          if (isDurationHabit) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                              modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                              Text(
                                text = "⏱️ موقوتة",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                              )
                            }
                          }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = "التقدم: ${habit.currentValue} / ${habit.targetValue} ${habit.unit} ${if (habit.isCompleted) "✅ (مكتملة)" else ""}",
                          fontSize = 12.sp,
                          color = if (habit.isCompleted) HabitEmerald else TextSecondary
                        )
                      }
                      if (isSelected) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = "محدد",
                          tint = HabitEmerald,
                          modifier = Modifier.size(18.dp)
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // Section 2: General Tasks (Accordion)
        item {
          val activeTasks = tasks.filter { !it.isCompleted }
          CollapsibleSectionCard(
            title = "المهام العامة",
            emoji = "⚡",
            count = activeTasks.size,
            isExpanded = tasksExpanded,
            accentColor = TaskViolet,
            onToggleExpand = { tasksExpanded = !tasksExpanded }
          ) {
            Column(
              modifier = Modifier.padding(top = 8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              if (activeTasks.isEmpty()) {
                Text(
                  text = "لا توجد مهام نشطة حالياً",
                  fontSize = 13.sp,
                  color = TextMuted,
                  modifier = Modifier.padding(8.dp)
                )
              } else {
                activeTasks.forEach { task ->
                  val isSelected = currentAttachment.type == FocusAttachType.GENERAL_TASK &&
                    currentAttachment.id == task.id

                  Surface(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .clickable {
                        onSelectAttachment(
                          FocusAttachment(
                            type = FocusAttachType.GENERAL_TASK,
                            id = task.id,
                            title = task.title,
                            iconEmoji = "⚡"
                          )
                        )
                        onDismiss()
                      },
                    color = if (isSelected) TaskVioletBg else SurfaceWhite,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                      1.dp,
                      if (isSelected) TaskViolet else BorderLight
                    )
                  ) {
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Box(
                        modifier = Modifier
                          .size(32.dp)
                          .clip(CircleShape)
                          .background(TaskVioletBg),
                        contentAlignment = Alignment.Center
                      ) {
                        Text(text = "⚡", fontSize = 16.sp)
                      }
                      Spacer(modifier = Modifier.width(10.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Text(
                          text = task.title,
                          fontSize = 14.sp,
                          fontWeight = FontWeight.SemiBold,
                          color = TextPrimary
                        )
                        Text(
                          text = "أولوية ${task.priority.titleArabic} • ${task.schedule.titleArabic}",
                          fontSize = 12.sp,
                          color = TextSecondary
                        )
                      }
                      if (isSelected) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = "محدد",
                          tint = TaskViolet,
                          modifier = Modifier.size(18.dp)
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // Section 3: Goals & Tasks (Accordion)
        item {
          val activeGoals = goals.filter { !it.isPaused }
          CollapsibleSectionCard(
            title = "الأهداف ومهامها",
            emoji = "🎯",
            count = activeGoals.size,
            isExpanded = goalsExpanded,
            accentColor = BrightBlue,
            onToggleExpand = { goalsExpanded = !goalsExpanded }
          ) {
            Column(
              modifier = Modifier.padding(top = 8.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              if (activeGoals.isEmpty()) {
                Text(
                  text = "لا توجد أهداف نشطة حالياً",
                  fontSize = 13.sp,
                  color = TextMuted,
                  modifier = Modifier.padding(8.dp)
                )
              } else {
                activeGoals.forEach { goal ->
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .background(Color(0xFFF8FAFC))
                      .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                      .padding(10.dp)
                  ) {
                    // Goal Header
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Text(text = goal.iconEmoji.ifEmpty { "🎯" }, fontSize = 20.sp)
                      Spacer(modifier = Modifier.width(8.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Text(
                          text = goal.title,
                          fontSize = 14.sp,
                          fontWeight = FontWeight.Bold,
                          color = TextPrimary
                        )
                        Text(
                          text = "نسبة الإنجاز ${goal.progressPercentage}%",
                          fontSize = 11.sp,
                          color = TextSecondary
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Goal's Tasks List
                    val goalTasks = goal.tasks.filter { !it.isCompleted }
                    if (goalTasks.isEmpty()) {
                      Text(
                        text = "لا توجد مهام متبقية في هذا الهدف",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      )
                    } else {
                      goalTasks.forEach { task ->
                        val isSelected = currentAttachment.type == FocusAttachType.GOAL_TASK &&
                          currentAttachment.id == task.id

                        Surface(
                          modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                              onSelectAttachment(
                                FocusAttachment(
                                  type = FocusAttachType.GOAL_TASK,
                                  id = task.id,
                                  goalId = goal.id,
                                  title = "${task.title} (${goal.title})",
                                  iconEmoji = goal.iconEmoji.ifEmpty { "🎯" }
                                )
                              )
                              onDismiss()
                            },
                          color = if (isSelected) GoalBlueBg else SurfaceWhite,
                          shape = RoundedCornerShape(8.dp),
                          border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) BrightBlue else BorderLight.copy(alpha = 0.6f)
                          )
                        ) {
                          Row(
                            modifier = Modifier
                              .fillMaxWidth()
                              .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                          ) {
                            Text(text = "•", fontSize = 16.sp, color = BrightBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                              text = task.title,
                              fontSize = 13.sp,
                              fontWeight = FontWeight.Medium,
                              color = TextPrimary,
                              modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                              Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "محدد",
                                tint = BrightBlue,
                                modifier = Modifier.size(16.dp)
                              )
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun CollapsibleSectionCard(
  title: String,
  emoji: String,
  count: Int,
  isExpanded: Boolean,
  accentColor: Color,
  onToggleExpand: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(Color(0xFFFAFAFA))
      .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
      .padding(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onToggleExpand() },
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(accentColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = "$count",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
          )
        }
      }
      Icon(
        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = if (isExpanded) "طي" else "توسيع",
        tint = TextSecondary
      )
    }

    AnimatedVisibility(
      visible = isExpanded,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + shrinkVertically()
    ) {
      content()
    }
  }
}
