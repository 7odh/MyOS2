package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.ScreenDestination
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.ui.components.CreateTaskBottomSheet
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.MyOSHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.components.TaskDetailCard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.viewmodel.MyOSUiState
import com.example.viewmodel.TaskFilter
import com.example.viewmodel.TaskSortOrder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onToggleRestMode: () -> Unit,
  onSetTaskFilter: (TaskFilter) -> Unit,
  onSetTaskSortOrder: (TaskSortOrder) -> Unit,
  onOpenCreateTask: () -> Unit,
  onOpenEditTask: (Task) -> Unit,
  onCloseCreateTask: () -> Unit,
  onSaveTask: (
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) -> Unit,
  onDeleteTask: (String) -> Unit,
  onToggleTask: (String) -> Unit,
  onDismissNotification: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onScreenSelected(ScreenDestination.HOME)
  }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }
  var taskToDelete by remember { mutableStateOf<Task?>(null) }

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      onDismissNotification()
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(
        drawerContainerColor = SurfaceWhite
      ) {
        NavigationDrawerContent(
          currentScreen = uiState.currentScreen,
          onScreenSelected = onScreenSelected,
          onCloseDrawer = {
            scope.launch { drawerState.close() }
          }
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        MyOSHeader(
          isRestModeActive = uiState.user.isRestModeActive,
          onMenuClick = {
            scope.launch { drawerState.open() }
          },
          onRestModeToggle = onToggleRestMode,
          onSearchClick = {
            onScreenSelected(ScreenDestination.SEARCH)
          }
        )
      },
      bottomBar = {
        MyOSBottomNavigationBar(
          currentScreen = uiState.currentScreen,
          onTabSelected = onScreenSelected,
          onMoreClick = {
            scope.launch { drawerState.open() }
          }
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = onOpenCreateTask,
          shape = CircleShape,
          containerColor = Color.Transparent,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
          modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape)
            .background(
              brush = Brush.linearGradient(
                colors = listOf(
                  TaskViolet,
                  BrightBlue,
                  ElectricViolet
                )
              ),
              shape = CircleShape
            )
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "إضافة مهمة جديدة",
            tint = TextWhite,
            modifier = Modifier.size(28.dp)
          )
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
          // 1. Header Card with Summary Metrics
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
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(38.dp)
                      .clip(CircleShape)
                      .background(TaskVioletBg),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Outlined.CheckBox,
                      contentDescription = "المهام",
                      tint = TaskViolet,
                      modifier = Modifier.size(22.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "المهام وقائمة الإنجاز",
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                      ),
                      color = TextPrimary
                    )
                    Text(
                      text = "إدارة وتنظيم المهام اليومية والمؤجلة",
                      style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                      color = TextSecondary
                    )
                  }
                }

                Button(
                  onClick = onOpenCreateTask,
                  colors = ButtonDefaults.buttonColors(containerColor = TaskViolet),
                  shape = RoundedCornerShape(12.dp),
                  contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "مهمة جديدة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Stat metrics cards
              val generalTasks = uiState.generalTasks.filter { !it.isGoalTask }
              val totalCount = generalTasks.size
              val completedCount = generalTasks.count { it.isCompleted }
              val todayCount = generalTasks.count { it.schedule == TaskSchedule.TODAY }
              val noDateCount = generalTasks.count { it.schedule == TaskSchedule.NO_DATE }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                TaskMetricBox(
                  icon = Icons.Outlined.Today,
                  title = "مهام اليوم ⚡",
                  value = "$todayCount",
                  accentColor = TaskViolet,
                  modifier = Modifier.weight(1f)
                )

                TaskMetricBox(
                  icon = Icons.Outlined.Inbox,
                  title = "بدون موعد 📭",
                  value = "$noDateCount",
                  accentColor = BrightBlue,
                  modifier = Modifier.weight(1f)
                )

                TaskMetricBox(
                  icon = Icons.Outlined.CheckCircle,
                  title = "المكتملة ✅",
                  value = "$completedCount / $totalCount",
                  accentColor = HabitEmerald,
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 2. Filter Chips (Scrollable row)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            TaskFilter.entries.forEach { filter ->
              val isSelected = uiState.activeTaskFilter == filter
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) TaskViolet else Color(0xFFF1F5F9))
                  .border(
                    width = 1.dp,
                    color = if (isSelected) TaskViolet else BorderLight,
                    shape = RoundedCornerShape(12.dp)
                  )
                  .clickable { onSetTaskFilter(filter) }
                  .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = filter.titleArabic,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                  ),
                  color = if (isSelected) TextWhite else TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 3. Sorting Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Outlined.Sort,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "الترتيب:",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                ),
                color = TextSecondary
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              TaskSortOrder.entries.forEach { sortOrder ->
                val isSelected = uiState.taskSortOrder == sortOrder
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFFEDE9FE) else Color(0xFFF8FAFC))
                    .border(
                      width = 1.dp,
                      color = if (isSelected) TaskViolet.copy(alpha = 0.5f) else BorderLight,
                      shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSetTaskSortOrder(sortOrder) }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = sortOrder.titleArabic,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (isSelected) TaskViolet else TextSecondary
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4. Tasks List
          val displayList = uiState.filteredGeneralTasks

          if (displayList.isEmpty()) {
            // Empty State
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 36.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(TaskVioletBg),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Outlined.CheckBox,
                    contentDescription = null,
                    tint = TaskViolet,
                    modifier = Modifier.size(28.dp)
                  )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                  text = "لا توجد مهام ضمن هذا التصنيف",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "أضف مهامك المحددة بتاريخ أو بدون موعد لترتيب أولوياتك",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                  onClick = onOpenCreateTask,
                  colors = ButtonDefaults.buttonColors(containerColor = TaskViolet),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("إضافة مهمة جديدة", fontWeight = FontWeight.Bold)
                }
              }
            }
          } else {
            displayList.forEach { task ->
              TaskDetailCard(
                task = task,
                onToggle = { onToggleTask(task.id) },
                onEdit = { onOpenEditTask(task) },
                onDelete = { taskToDelete = task }
              )
              Spacer(modifier = Modifier.height(8.dp))
            }
          }

          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }

  // Create / Edit Task Bottom Sheet
  if (uiState.isCreateTaskSheetVisible) {
    CreateTaskBottomSheet(
      isVisible = true,
      editingTask = uiState.editingGeneralTask,
      onDismiss = onCloseCreateTask,
      onSaveTask = onSaveTask,
      onDeleteTask = { taskId ->
        onDeleteTask(taskId)
        onCloseCreateTask()
      }
    )
  }

  // Delete Task Confirmation Dialog
  taskToDelete?.let { task ->
    AlertDialog(
      onDismissRequest = { taskToDelete = null },
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
            text = "حذف المهمة",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
          )
        }
      },
      text = {
        Text(
          text = "هل أنت متأكد من رغبتك في حذف المهمة \"${task.title}\"؟",
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
          color = TextSecondary
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onDeleteTask(task.id)
            taskToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("تأكيد الحذف", fontWeight = FontWeight.Bold, color = TextWhite)
        }
      },
      dismissButton = {
        TextButton(onClick = { taskToDelete = null }) {
          Text("إلغاء", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
private fun TaskMetricBox(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .background(Color(0xFFF8FAFC))
      .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
      .padding(10.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = accentColor,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        ),
        color = TextPrimary
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = TextSecondary
      )
    }
  }
}
