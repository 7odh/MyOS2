package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Goal
import com.example.model.GoalIconProvider
import com.example.model.GoalStatus
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.components.AddGoalTaskDialog
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.CreateGoalBottomSheet
import com.example.ui.components.MyOSHeader
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.GoalBlueTrack
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.HabitEmeraldTrack
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.viewmodel.MyOSUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailsScreen(
  goal: Goal,
  uiState: MyOSUiState,
  onNavigateBack: () -> Unit,
  onToggleRestMode: () -> Unit,
  onToggleGoalTask: (taskId: String) -> Unit,
  onTogglePauseGoal: () -> Unit,
  onOpenEditGoal: () -> Unit,
  onCloseEditGoal: () -> Unit,
  onSaveGoal: (title: String, description: String?, iconId: String, priority: Priority, dueDate: String?) -> Unit,
  onOpenAddTask: () -> Unit,
  onOpenEditTask: (Task) -> Unit,
  onCloseAddTask: () -> Unit,
  onSaveTask: (title: String, notes: String?, priority: Priority, schedule: com.example.model.TaskSchedule, dueDateFormatted: String?) -> Unit,
  onDeleteGoal: () -> Unit,
  onDismissNotification: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onNavigateBack()
  }

  val snackbarHostState = remember { SnackbarHostState() }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var showDeleteConfirmation by remember { mutableStateOf(false) }
  val iconOption = GoalIconProvider.getIcon(goal.iconId)

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      onDismissNotification()
    }
  }

  val isCompleted = goal.status == GoalStatus.COMPLETED
  val isPaused = goal.status == GoalStatus.PAUSED

  val activeProgressColor = when {
    isCompleted -> HabitEmerald
    isPaused -> Color(0xFFD97706)
    else -> BrightBlue
  }

  val activeTrackColor = when {
    isCompleted -> HabitEmeraldTrack
    isPaused -> Color(0xFFFEF3C7)
    else -> GoalBlueTrack
  }

  Scaffold(
    topBar = {
      // Header with Back button, Logo, and "راحة" button
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(BackgroundLight)
          .statusBarsPadding()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Back button
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(44.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "الرجوع",
              tint = TextPrimary,
              modifier = Modifier.size(24.dp)
            )
          }

          // Center Logo and Title
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            com.example.ui.components.MyOSLogoIcon(size = 28.dp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "MyOS",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = TextPrimary
            )
          }

          // Rest Day Button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (uiState.user.isRestModeActive) Color(0xFF6366F1) else Color(0xFFF3F0FF))
              .clickable(onClick = onToggleRestMode)
              .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "راحة",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              ),
              color = if (uiState.user.isRestModeActive) TextWhite else Color(0xFF6366F1)
            )
          }
        }
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    containerColor = BackgroundLight,
    modifier = modifier
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentAlignment = Alignment.TopCenter
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 640.dp)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 1. Goal Summary Card
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(22.dp)),
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp)
          ) {
            // Header Row: Icon, Title + Edit button
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                // Goal Icon
                Box(
                  modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconOption.bgColor),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = iconOption.icon,
                    contentDescription = null,
                    tint = iconOption.tintColor,
                    modifier = Modifier.size(28.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = goal.title,
                      style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                      ),
                      color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(GoalBlueBg)
                        .clickable(onClick = onOpenEditGoal),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "تعديل الهدف",
                        tint = BrightBlue,
                        modifier = Modifier.size(15.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2))
                        .clickable { showDeleteConfirmation = true },
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "حذف الهدف",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(15.dp)
                      )
                    }
                  }

                  if (!goal.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                      text = goal.description,
                      style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                      ),
                      color = TextSecondary,
                      maxLines = 2
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Visualization Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Left: Circular Progress Ring
              CircularProgressRing(
                percentage = goal.progressPercentage,
                strokeColor = activeProgressColor,
                trackColor = activeTrackColor,
                ringSize = 64.dp,
                strokeWidth = 6.dp
              )

              Spacer(modifier = Modifier.width(16.dp))

              // Right: Linear Progress Bar & Completed info
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${goal.completedTasksCount} من ${goal.totalTasksCount} مهام مكتملة",
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    ),
                    color = TextPrimary
                  )
                  Text(
                    text = "${goal.progressPercentage}%",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp
                    ),
                    color = activeProgressColor
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                  progress = { (goal.progressPercentage / 100f).coerceIn(0f, 1f) },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                  color = activeProgressColor,
                  trackColor = activeTrackColor,
                  strokeCap = StrokeCap.Round
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Metadata Chips Row (Start date, Due date, Priority)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Start Date Chip
              MetadataCard(
                icon = Icons.Outlined.CalendarMonth,
                title = "تاريخ البدء",
                value = goal.startDate ?: "2025/09/20",
                modifier = Modifier.weight(1f)
              )

              // Due Date Chip
              MetadataCard(
                icon = Icons.Outlined.CalendarMonth,
                title = "تاريخ الاستحقاق",
                value = goal.dueDate ?: "بدون موعد",
                modifier = Modifier.weight(1f)
              )

              // Priority Chip
              MetadataCard(
                icon = Icons.Outlined.LocalOffer,
                title = "الأولوية",
                value = goal.priority.titleArabic,
                valueColor = when (goal.priority) {
                  Priority.HIGH -> PriorityHigh
                  Priority.MEDIUM -> Color(0xFFD97706)
                  Priority.LOW -> PriorityLow
                  Priority.NONE -> TextSecondary
                },
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pause / Shelve Goal Action Button
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (goal.isPaused) Color(0xFFEFF6FF) else Color(0xFFFFFBEB))
                .border(
                  width = 1.dp,
                  color = if (goal.isPaused) BrightBlue.copy(alpha = 0.3f) else Color(0xFFFDE68A),
                  shape = RoundedCornerShape(14.dp)
                )
                .clickable(onClick = onTogglePauseGoal)
                .padding(vertical = 10.dp, horizontal = 14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (goal.isPaused) Icons.Outlined.PlayArrow else Icons.Outlined.Inventory2,
                  contentDescription = null,
                  tint = if (goal.isPaused) BrightBlue else Color(0xFFD97706),
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = if (goal.isPaused) "استئناف الهدف (إعادته للنشاط)" else "ركن الهدف مؤقتاً (حفظ التقدم والتاريخ)",
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  ),
                  color = if (goal.isPaused) BrightBlue else Color(0xFFD97706)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Navigation Tabs (المهام, التفاصيل, الإحصائيات)
        val tabs = listOf("المهام", "التفاصيل", "الإحصائيات")
        TabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = SurfaceWhite,
          contentColor = BrightBlue,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
              color = BrightBlue,
              height = 3.dp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
        ) {
          tabs.forEachIndexed { index, tabTitle ->
            Tab(
              selected = selectedTabIndex == index,
              onClick = { selectedTabIndex = index },
              text = {
                Text(
                  text = tabTitle,
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                  ),
                  color = if (selectedTabIndex == index) BrightBlue else TextSecondary
                )
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Tab Contents
        when (selectedTabIndex) {
          0 -> {
            // "مهام الهدف" Tab
            Column(modifier = Modifier.fillMaxWidth()) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = null,
                    tint = BrightBlue,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "مهام الهدف",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp
                    ),
                    color = TextPrimary
                  )
                }

                Text(
                  text = "${goal.totalTasksCount} مهام",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                  color = TextSecondary
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              // "+ إضافة مهمة جديدة" Button Card
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(Color(0xFFEFF6FF))
                  .border(1.dp, BrightBlue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                  .clickable(onClick = onOpenAddTask)
                  .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = BrightBlue,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "إضافة مهمة جديدة",
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp
                    ),
                    color = BrightBlue
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Tasks List: sorted by priority (High -> Medium -> Low -> None)
              val sortedTasks = goal.sortedTasks
              if (sortedTasks.isEmpty()) {
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Text(
                      text = "لا توجد مهام مسجلة لهذا الهدف بعد.",
                      style = MaterialTheme.typography.bodyMedium,
                      color = TextSecondary
                    )
                  }
                }
              } else {
                sortedTasks.forEach { task ->
                  GoalDetailTaskRow(
                    task = task,
                    onToggle = { onToggleGoalTask(task.id) },
                    onEdit = { onOpenEditTask(task) }
                  )
                  Spacer(modifier = Modifier.height(8.dp))
                }
              }
            }
          }
          1 -> {
            // "التفاصيل" Tab
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(18.dp)),
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp)
              ) {
                Text(
                  text = "وصف الهدف",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = goal.description ?: "لا يوجد وصف مدخل لهذا الهدف بعد.",
                  style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
                  color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = "حالة الهدف",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = if (goal.isPaused) "الهدف حالياً متوقف مؤقتاً ومحفوظ على الرف. يمكنك استئنافه في أي وقت." else "الهدف قيد التنفيذ النشط ومهامه المجدولة لليوم تظهر في لوحة التحكم.",
                  style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                  color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFEF2F2))
                    .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(14.dp))
                    .clickable { showDeleteConfirmation = true }
                    .padding(vertical = 12.dp, horizontal = 14.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Outlined.DeleteOutline,
                      contentDescription = null,
                      tint = Color(0xFFEF4444),
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "حذف هذا الهدف نهائياً",
                      style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                      ),
                      color = Color(0xFFEF4444)
                    )
                  }
                }
              }
            }
          }
          2 -> {
            // "الإحصائيات" Tab
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(18.dp)),
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp)
              ) {
                Text(
                  text = "إحصائيات الإنجاز",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("نسبة الإنجاز الكلية:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                  Text("${goal.progressPercentage}%", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = activeProgressColor)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("المهام المكتملة:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                  Text("${goal.completedTasksCount} مهمة", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("المهام المتبقية:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                  Text("${goal.totalTasksCount - goal.completedTasksCount} مهمة", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }

  // Edit Goal Bottom Sheet
  if (uiState.isCreateGoalSheetVisible) {
    CreateGoalBottomSheet(
      isVisible = true,
      editingGoal = uiState.editingGoal,
      onDismiss = onCloseEditGoal,
      onSaveGoal = onSaveGoal
    )
  }

  // Add / Edit Goal Task Dialog
  if (uiState.isAddGoalTaskDialogVisible) {
    AddGoalTaskDialog(
      editingTask = uiState.editingGoalTask,
      onDismiss = onCloseAddTask,
      onSaveTask = onSaveTask
    )
  }

  // Delete Goal Confirmation Dialog
  if (showDeleteConfirmation) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirmation = false },
      shape = RoundedCornerShape(20.dp),
      containerColor = SurfaceWhite,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.DeleteOutline,
            contentDescription = null,
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "حذف الهدف",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = TextPrimary
          )
        }
      },
      text = {
        Text(
          text = "هل أنت متأكد من رغبتك في حذف هدف \"${goal.title}\"؟\nسيتم حذف الهدف وكافة المهام التابعة له نهائياً ولا يمكن التراجع عن هذا الإجراء.",
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
          color = TextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showDeleteConfirmation = false
            onDeleteGoal()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("تأكيد الحذف", fontWeight = FontWeight.Bold, color = TextWhite)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirmation = false }) {
          Text("إلغاء", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
private fun MetadataCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String,
  valueColor: Color = TextPrimary,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFFF8FAFC))
      .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = TextSecondary,
          modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = TextSecondary
        )
      }
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        ),
        color = valueColor,
        maxLines = 1
      )
    }
  }
}

@Composable
private fun GoalDetailTaskRow(
  task: Task,
  onToggle: () -> Unit,
  onEdit: () -> Unit,
  modifier: Modifier = Modifier
) {
  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) BrightBlue else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) BrightBlue else BorderLight,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.7f), RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onToggle)
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Checkbox circle
      Box(
        modifier = Modifier
          .size(22.dp)
          .clip(CircleShape)
          .background(checkBgColor)
          .border(1.5.dp, checkBorderColor, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        if (task.isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "مكتملة",
            tint = TextWhite,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Title & Date
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
            fontSize = 14.sp,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
          ),
          color = if (task.isCompleted) TextSecondary.copy(alpha = 0.65f) else TextPrimary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = task.dueDateFormatted ?: "بدون تاريخ",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
          ),
          color = TextSecondary
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Priority Badge
      val (priorityText, priorityTextColor, priorityBg) = when (task.priority) {
        Priority.HIGH -> Triple("عالية", PriorityHigh, Color(0xFFFEF2F2))
        Priority.MEDIUM -> Triple("متوسطة", Color(0xFFD97706), Color(0xFFFFFBEB))
        Priority.LOW -> Triple("منخفضة", PriorityLow, Color(0xFFF1F5F9))
        Priority.NONE -> Triple("بدون أولوية", TextSecondary, Color(0xFFF8FAFC))
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(priorityBg)
          .padding(horizontal = 8.dp, vertical = 3.dp)
      ) {
        Text(
          text = priorityText,
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          ),
          color = priorityTextColor
        )
      }

      Spacer(modifier = Modifier.width(4.dp))

      // Edit action
      IconButton(
        onClick = onEdit,
        modifier = Modifier.size(32.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.Edit,
          contentDescription = "تعديل المهمة",
          tint = TextSecondary.copy(alpha = 0.7f),
          modifier = Modifier.size(15.dp)
        )
      }
    }
  }
}
