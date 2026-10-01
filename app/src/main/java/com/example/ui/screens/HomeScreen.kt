package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.DayOfWeekArabic
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.model.QuickAddType
import com.example.model.ScreenDestination
import com.example.model.TaskSchedule
import com.example.ui.components.AddItemDialog
import com.example.ui.components.CreateGoalBottomSheet
import com.example.ui.components.CreateHabitBottomSheet
import com.example.ui.components.CreateTaskBottomSheet
import com.example.ui.components.DailySummarySection
import com.example.ui.components.EditNameDialog
import com.example.ui.components.LogHabitProgressDialog
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.MyOSHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.components.QuickAddBottomSheet
import com.example.ui.components.TodaySection
import com.example.ui.components.WelcomeCard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextWhite
import com.example.viewmodel.MyOSUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onToggleRestMode: () -> Unit,
  onEditNameClick: () -> Unit,
  onSaveName: (String) -> Unit,
  onDismissEditName: () -> Unit,
  onOpenQuickAdd: () -> Unit,
  onCloseQuickAdd: () -> Unit,
  onQuickAddOptionSelected: (QuickAddType) -> Unit,
  onCloseCreationDialog: () -> Unit,
  onAddTask: (String, Priority) -> Unit,
  onAddHabit: (String, Priority) -> Unit,
  onAddGoal: (String, List<String>, Priority) -> Unit,
  onIncrementHabit: (String) -> Unit,
  onOpenLogHabitDialog: (Habit) -> Unit,
  onCloseLogHabitDialog: () -> Unit,
  onSaveHabitProgress: (String, Int) -> Unit,
  onToggleHabitBoolean: (String) -> Unit,
  onToggleTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit = {},
  onPostponeGoalTask: (String, String) -> Unit = { _, _ -> },
  onDismissNotification: () -> Unit,
  onCloseCreateTask: () -> Unit = {},
  onSaveGeneralTask: (
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) -> Unit = { _, _, _, _, _ -> },
  onCloseCreateGoal: () -> Unit = {},
  onSaveGoalFull: (
    title: String,
    description: String?,
    iconId: String,
    priority: Priority,
    dueDate: String?
  ) -> Unit = { _, _, _, _, _ -> },
  onCloseCreateHabit: () -> Unit = {},
  onSaveHabitFull: (
    title: String,
    type: HabitType,
    targetValue: Int,
    unit: String,
    frequency: HabitFrequency,
    scheduledDays: List<DayOfWeekArabic>,
    isMandatory: Boolean,
    priority: Priority,
    iconEmoji: String
  ) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
  modifier: Modifier = Modifier
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

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
          onClick = onOpenQuickAdd,
          shape = CircleShape,
          containerColor = Color.Transparent,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
          modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape)
            .background(
              brush = Brush.linearGradient(
                colors = listOf(
                  BrightBlue,
                  DeepBlue,
                  ElectricViolet
                )
              ),
              shape = CircleShape
            )
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "إضافة سريعة",
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
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 1. Welcome Card
          WelcomeCard(
            userName = uiState.user.name,
            motivationalSentence = uiState.user.motivationalSentence,
            isMotivationEnabled = uiState.user.isMotivationEnabled,
            onEditNameClick = onEditNameClick
          )

          Spacer(modifier = Modifier.height(4.dp))

          // 2. Daily Analytics Summary Section
          DailySummarySection(
            analytics = uiState.dailyAnalytics,
            onNavigateToAnalytics = {
              onScreenSelected(ScreenDestination.ANALYTICS)
            }
          )

          Spacer(modifier = Modifier.height(6.dp))

          // 3. "عليك اليوم" Section (Habits, General Tasks, Goals)
          TodaySection(
            habits = uiState.habits,
            tasks = uiState.generalTasks,
            goals = uiState.goals,
            isRestModeActive = uiState.user.isRestModeActive,
            onIncrementHabit = onIncrementHabit,
            onOpenLogHabitDialog = onOpenLogHabitDialog,
            onToggleHabitBoolean = onToggleHabitBoolean,
            onToggleTask = onToggleTask,
            onToggleGoalTask = onToggleGoalTask,
            onPostponeTask = onPostponeTask,
            onPostponeGoalTask = onPostponeGoalTask,
            onViewAllClick = {
              onScreenSelected(ScreenDestination.GOALS)
            },
            onNavigateToTasks = {
              onScreenSelected(ScreenDestination.TASKS)
            }
          )

          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }

  // Quick Add Bottom Sheet
  QuickAddBottomSheet(
    isVisible = uiState.isQuickAddSheetVisible,
    onDismiss = onCloseQuickAdd,
    onOptionSelected = onQuickAddOptionSelected
  )

  // Edit Name Dialog
  if (uiState.isEditNameDialogVisible) {
    EditNameDialog(
      currentName = uiState.user.name,
      onDismiss = onDismissEditName,
      onSave = onSaveName
    )
  }

  // Add Item Dialog
  uiState.activeCreationDialog?.let { type ->
    AddItemDialog(
      type = type,
      onDismiss = onCloseCreationDialog,
      onAddTask = onAddTask,
      onAddHabit = onAddHabit,
      onAddGoal = onAddGoal
    )
  }

  // Log Habit Progress Dialog (for Quantity & Duration habits)
  uiState.selectedHabitToLog?.let { habitToLog ->
    LogHabitProgressDialog(
      habit = habitToLog,
      onDismiss = onCloseLogHabitDialog,
      onSaveProgress = { newTotal ->
        onSaveHabitProgress(habitToLog.id, newTotal)
      }
    )
  }

  // Create Task Bottom Sheet (from Quick Add)
  if (uiState.isCreateTaskSheetVisible) {
    CreateTaskBottomSheet(
      isVisible = true,
      editingTask = uiState.editingGeneralTask,
      onDismiss = onCloseCreateTask,
      onSaveTask = onSaveGeneralTask
    )
  }

  // Create Goal Bottom Sheet (from Quick Add)
  if (uiState.isCreateGoalSheetVisible) {
    CreateGoalBottomSheet(
      isVisible = true,
      editingGoal = uiState.editingGoal,
      onDismiss = onCloseCreateGoal,
      onSaveGoal = onSaveGoalFull
    )
  }

  // Create Habit Bottom Sheet (from Quick Add)
  if (uiState.isCreateHabitSheetVisible) {
    CreateHabitBottomSheet(
      isVisible = true,
      editingHabit = uiState.editingHabit,
      onDismiss = onCloseCreateHabit,
      onSaveHabit = onSaveHabitFull
    )
  }
}
