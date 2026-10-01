package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun HabitDetailCard(
  habit: Habit,
  isRestModeActive: Boolean,
  onIncrementCounter: () -> Unit,
  onOpenLogDialog: () -> Unit,
  onToggleBoolean: () -> Unit,
  onEditHabit: () -> Unit,
  onDeleteHabit: () -> Unit,
  onOpenPauseDialog: () -> Unit = {},
  onResumeHabit: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val isCompleted = habit.isCompleted
  val isExceeded = habit.currentValue > habit.targetValue
  val isOnRestToday = isRestModeActive && !habit.isMandatory
  val isPaused = habit.isCurrentlyPaused

  val cardBorderColor by animateColorAsState(
    targetValue = when {
      isPaused -> Color(0xFFF59E0B).copy(alpha = 0.5f)
      isCompleted -> HabitEmerald.copy(alpha = 0.5f)
      isOnRestToday -> Color(0xFFD97706).copy(alpha = 0.3f)
      else -> BorderLight.copy(alpha = 0.8f)
    },
    animationSpec = tween(250),
    label = "cardBorderColor"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, cardBorderColor, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = when {
        isPaused -> Color(0xFFFFFDF5)
        isCompleted -> Color(0xFFF9FDFB)
        isOnRestToday -> Color(0xFFFFFDF5)
        else -> SurfaceCard
      }
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 1. Top row: Icon, Title, Pause, Edit & Delete Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Icon badge
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(if (isCompleted) HabitEmeraldBg else Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = habit.iconEmoji, fontSize = 22.sp)
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = habit.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              ),
              color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Schedule & frequency info
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = habit.scheduleDescription,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = TextSecondary
              )
              if (habit.currentStreak > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFF97316),
                    modifier = Modifier.size(13.dp)
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                    text = "${habit.currentStreak} يوم",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    ),
                    color = Color(0xFFEA580C)
                  )
                }
              }
            }
          }
        }

        // Action buttons: Pause/Resume, Edit, Delete
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isPaused) {
            // Resume Action
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFDCFCE7))
                .clickable(onClick = onResumeHabit)
                .padding(horizontal = 8.dp, vertical = 5.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = "استئناف",
                  tint = HabitEmerald,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "استئناف",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  ),
                  color = HabitEmerald
                )
              }
            }
          } else {
            // Pause Action
            Box(
              modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color(0xFFFEF3C7))
                .clickable(onClick = onOpenPauseDialog),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.PauseCircleOutline,
                contentDescription = "ركن العادة مؤقتاً",
                tint = Color(0xFFD97706),
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(CircleShape)
              .background(Color(0xFFEFF6FF))
              .clickable(onClick = onEditHabit),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "تعديل",
              tint = BrightBlue,
              modifier = Modifier.size(15.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(CircleShape)
              .background(Color(0xFFFEE2E2))
              .clickable(onClick = onDeleteHabit),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = "حذف",
              tint = Color(0xFFEF4444),
              modifier = Modifier.size(15.dp)
            )
          }
        }
      }

      // Paused Banner if habit is currently paused
      if (isPaused) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF3C7))
            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Outlined.PauseCircleOutline,
                contentDescription = null,
                tint = Color(0xFFB45309),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = habit.pauseStatusDescription ?: "العادة مركونة مؤقتاً لتخفيف الضغوط",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = Color(0xFF92400E)
              )
            }

            TextButton(
              onClick = onResumeHabit,
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "استئناف الآن ▶️",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = Color(0xFFB45309)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 2. Badges Row: Type badge, Mandatory badge or Rest day badge, Priority badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Habit Type badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
          Text(
            text = habit.type.titleArabic,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
            color = TextSecondary
          )
        }

        // Mandatory vs Rest Mode vs Paused Badge
        if (isPaused) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFFEF3C7))
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(
              text = "مركونة ⏸️",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              ),
              color = Color(0xFFB45309)
            )
          }
        } else if (habit.isMandatory) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(HabitEmeraldBg)
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(
              text = "إجبارية 🛡️",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              ),
              color = HabitEmerald
            )
          }
        } else if (isOnRestToday) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFFEF3C7))
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(
              text = "يوم راحة مستحق ☕",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              ),
              color = Color(0xFFD97706)
            )
          }
        }

        // Priority badge
        val (pText, pColor, pBg) = when (habit.priority) {
          Priority.HIGH -> Triple("أولوية عالية", PriorityHigh, Color(0xFFFEF2F2))
          Priority.MEDIUM -> Triple("أولوية متوسطة", Color(0xFFD97706), Color(0xFFFFFBEB))
          Priority.LOW -> Triple("أولوية منخفضة", PriorityLow, Color(0xFFF1F5F9))
          Priority.NONE -> Triple("عادية", TextSecondary, Color(0xFFF8FAFC))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(pBg)
            .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
          Text(
            text = pText,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = pColor
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3. Progress Row & Quick Log Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Progress text
        val progressText = when {
          isPaused -> "العادة مركونة مؤقتاً"
          habit.type == HabitType.BOOLEAN -> if (isCompleted) "مكتملة اليوم ✨" else "لم تكتمل بعد"
          else -> "${habit.currentValue} من ${habit.targetValue} ${habit.unit}"
        }

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "الإنجاز اليومي:",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = TextSecondary
            )
            Text(
              text = progressText,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = when {
                isPaused -> Color(0xFFB45309)
                isExceeded -> HabitEmerald
                isCompleted -> HabitEmerald
                else -> BrightBlue
              }
            )
          }

          if (habit.type != HabitType.BOOLEAN) {
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = {
                if (habit.targetValue <= 0) 0f
                else (habit.currentValue.toFloat() / habit.targetValue.toFloat()).coerceIn(0f, 1f)
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = if (isCompleted) HabitEmerald else BrightBlue,
              trackColor = if (isCompleted) HabitEmeraldTrack else Color(0xFFE2E8F0),
              strokeCap = StrokeCap.Round
            )
          }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Interaction Button based on habit type (or Resume button if paused)
        if (isPaused) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFDCFCE7))
              .border(
                width = 1.dp,
                color = HabitEmerald.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
              )
              .clickable(onClick = onResumeHabit)
              .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = HabitEmerald,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "استئناف",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = HabitEmerald
              )
            }
          }
        } else {
          when (habit.type) {
            HabitType.COUNTER -> {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isCompleted) HabitEmeraldBg else Color(0xFFEFF6FF))
                  .border(
                    width = 1.dp,
                    color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrightBlue.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                  )
                  .clickable(onClick = onIncrementCounter)
                  .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (isCompleted) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = HabitEmerald,
                      modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                  }
                  Text(
                    text = "+1 ${habit.unit}",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    ),
                    color = if (isCompleted) HabitEmerald else BrightBlue
                  )
                }
              }
            }
            HabitType.QUANTITY, HabitType.DURATION -> {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isCompleted) HabitEmeraldBg else Color(0xFFEFF6FF))
                  .border(
                    width = 1.dp,
                    color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrightBlue.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                  )
                  .clickable(onClick = onOpenLogDialog)
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (isCompleted) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = HabitEmerald,
                      modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                  }
                  Text(
                    text = if (habit.type == HabitType.DURATION) "تسجيل وقت" else "تسجيل إنجاز",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    ),
                    color = if (isCompleted) HabitEmerald else BrightBlue
                  )
                }
              }
            }
            HabitType.BOOLEAN -> {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isCompleted) HabitEmerald else Color.Transparent)
                  .border(
                    1.5.dp,
                    if (isCompleted) HabitEmerald else BorderLight,
                    CircleShape
                  )
                  .clickable(onClick = onToggleBoolean),
                contentAlignment = Alignment.Center
              ) {
                if (isCompleted) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "مكتملة",
                    tint = TextWhite,
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
}
