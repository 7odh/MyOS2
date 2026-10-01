package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ScreenDestination
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.GoalDetailsScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.HabitsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PlaceholderScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MyOSViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: MyOSViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        // Enforce RTL layout direction to match the Arabic design specification
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundLight
          ) {
            MyOSApp(viewModel = viewModel)
          }
        }
      }
    }
  }
}

@Composable
fun MyOSApp(viewModel: MyOSViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val selectedGoal = uiState.selectedGoal
  if (selectedGoal != null) {
    // Goal Details Screen (Dedicated page for the selected goal)
    GoalDetailsScreen(
      goal = selectedGoal,
      uiState = uiState,
      onNavigateBack = viewModel::onClearSelectedGoal,
      onToggleRestMode = viewModel::onToggleRestMode,
      onToggleGoalTask = { taskId -> viewModel.toggleGoalTask(selectedGoal.id, taskId) },
      onTogglePauseGoal = { viewModel.toggleGoalPause(selectedGoal.id) },
      onOpenEditGoal = { viewModel.openEditGoal(selectedGoal) },
      onCloseEditGoal = viewModel::closeCreateGoalSheet,
      onSaveGoal = { title, description, iconId, priority, dueDate ->
        viewModel.saveGoal(title, description, iconId, priority, dueDate, selectedGoal.category)
      },
      onOpenAddTask = viewModel::openAddGoalTaskDialog,
      onOpenEditTask = viewModel::openEditGoalTask,
      onCloseAddTask = viewModel::closeAddGoalTaskDialog,
      onSaveTask = { title, notes, priority, schedule, dueDateFormatted ->
        viewModel.saveGoalTask(selectedGoal.id, title, notes, priority, schedule, dueDateFormatted)
      },
      onDeleteGoal = {
        viewModel.deleteGoal(selectedGoal.id)
      },
      onDismissNotification = viewModel::dismissNotification
    )
  } else {
    when (uiState.currentScreen) {
      ScreenDestination.HOME, ScreenDestination.MORE -> {
        HomeScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onEditNameClick = viewModel::openEditNameDialog,
          onSaveName = viewModel::saveUserName,
          onDismissEditName = viewModel::closeEditNameDialog,
          onOpenQuickAdd = viewModel::openQuickAddSheet,
          onCloseQuickAdd = viewModel::closeQuickAddSheet,
          onQuickAddOptionSelected = viewModel::onQuickAddOptionSelected,
          onCloseCreationDialog = viewModel::closeCreationDialog,
          onAddTask = viewModel::addTask,
          onAddHabit = viewModel::addHabit,
          onAddGoal = { title, tasks, priority ->
            viewModel.saveGoal(title, null, "target", priority, null)
          },
          onIncrementHabit = viewModel::incrementHabit,
          onOpenLogHabitDialog = viewModel::openLogHabitDialog,
          onCloseLogHabitDialog = viewModel::closeLogHabitDialog,
          onSaveHabitProgress = viewModel::updateHabitProgress,
          onToggleHabitBoolean = viewModel::toggleHabitBoolean,
          onToggleTask = viewModel::toggleGeneralTask,
          onToggleGoalTask = viewModel::toggleGoalTask,
          onPostponeTask = viewModel::postponeGeneralTask,
          onPostponeGoalTask = viewModel::postponeGoalTask,
          onDismissNotification = viewModel::dismissNotification,
          onCloseCreateTask = viewModel::closeCreateTaskSheet,
          onSaveGeneralTask = viewModel::saveGeneralTask,
          onCloseCreateGoal = viewModel::closeCreateGoalSheet,
          onSaveGoalFull = viewModel::saveGoal,
          onCloseCreateHabit = viewModel::closeCreateHabitSheet,
          onSaveHabitFull = viewModel::saveHabit
        )
      }
      ScreenDestination.CALENDAR -> {
        CalendarScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onSelectDate = viewModel::onSelectCalendarDate,
          onChangeViewMode = viewModel::onChangeCalendarViewMode,
          onToggleTask = viewModel::toggleGeneralTask,
          onToggleGoalTask = viewModel::toggleGoalTask,
          onPostponeTask = viewModel::postponeGeneralTask,
          onPostponeGoalTask = viewModel::postponeGoalTask,
          onIncrementHabit = viewModel::incrementHabit,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.GOALS -> {
        GoalsScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onSelectGoal = viewModel::onSelectGoal,
          onSetGoalFilter = viewModel::setGoalFilter,
          onOpenCreateGoal = viewModel::openCreateGoalSheet,
          onCloseCreateGoal = viewModel::closeCreateGoalSheet,
          onSaveGoal = viewModel::saveGoal,
          onDeleteGoal = viewModel::deleteGoal,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.HABITS -> {
        HabitsScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onSetHabitFilter = viewModel::setHabitFilter,
          onOpenCreateHabit = viewModel::openCreateHabitSheet,
          onOpenEditHabit = viewModel::openEditHabit,
          onCloseCreateHabit = viewModel::closeCreateHabitSheet,
          onSaveHabit = viewModel::saveHabit,
          onDeleteHabit = viewModel::deleteHabit,
          onIncrementHabit = viewModel::incrementHabit,
          onOpenLogHabitDialog = viewModel::openLogHabitDialog,
          onCloseLogHabitDialog = viewModel::closeLogHabitDialog,
          onSaveHabitProgress = viewModel::updateHabitProgress,
          onToggleHabitBoolean = viewModel::toggleHabitBoolean,
          onOpenPauseHabitDialog = viewModel::openPauseHabitDialog,
          onClosePauseHabitDialog = viewModel::closePauseHabitDialog,
          onConfirmPauseHabit = viewModel::pauseHabit,
          onResumeHabit = viewModel::resumeHabit,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.TASKS -> {
        TasksScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onSetTaskFilter = viewModel::setTaskFilter,
          onSetTaskSortOrder = viewModel::setTaskSortOrder,
          onOpenCreateTask = viewModel::openCreateTaskSheet,
          onOpenEditTask = viewModel::openEditTask,
          onCloseCreateTask = viewModel::closeCreateTaskSheet,
          onSaveTask = viewModel::saveGeneralTask,
          onDeleteTask = viewModel::deleteGeneralTask,
          onToggleTask = viewModel::toggleGeneralTask,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.NOTES -> {
        NotesScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onOpenCreateNote = viewModel::openCreateNoteSheet,
          onOpenEditNote = viewModel::openEditNote,
          onCloseCreateNote = viewModel::closeCreateNoteSheet,
          onSaveNote = viewModel::saveNote,
          onTogglePinNote = viewModel::togglePinNote,
          onDeleteNote = viewModel::deleteNote,
          onSetActiveTag = viewModel::setActiveNoteTag,
          onSetSearchQuery = viewModel::setNoteSearchQuery,
          onConvertToGoal = viewModel::convertNoteToGoal,
          onConvertToHabit = viewModel::convertNoteToHabit,
          onConvertToTask = viewModel::convertNoteToTask,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.FOCUS -> {
        FocusScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onSetFocusMode = viewModel::setFocusMode,
          onSetFocusDurationMinutes = viewModel::setFocusDurationMinutes,
          onStartTimer = viewModel::startFocusTimer,
          onPauseTimer = viewModel::pauseFocusTimer,
          onResetTimer = viewModel::resetFocusTimer,
          onFinishEarly = viewModel::finishEarlyAndRecordProgress,
          onSetAttachment = viewModel::setFocusAttachment,
          onClearAttachment = viewModel::clearFocusAttachment,
          onSelectSound = viewModel::setAmbientSound,
          onToggleAttachPicker = viewModel::toggleAttachPicker,
          onToggleCustomDurationDialog = viewModel::toggleCustomDurationDialog,
          onToggleFlipClockFullScreen = viewModel::toggleFlipClockFullScreen,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.ANALYTICS -> {
        AnalyticsScreen(
          uiState = uiState,
          onScreenSelected = viewModel::onScreenSelected,
          onToggleRestMode = viewModel::onToggleRestMode,
          onSelectPeriod = viewModel::setAnalyticsPeriod,
          onDismissNotification = viewModel::dismissNotification
        )
      }
      ScreenDestination.SEARCH -> {
        SearchScreen(
          uiState = uiState,
          onQueryChanged = viewModel::onSearchQueryChanged,
          onCategorySelected = viewModel::onSearchCategorySelected,
          onExecuteSearch = viewModel::onExecuteSearch,
          onClearQuery = viewModel::onClearSearchQuery,
          onClearRecentSearches = viewModel::onClearRecentSearches,
          onNavigateBack = { viewModel.onScreenSelected(ScreenDestination.HOME) },
          onSelectGoal = viewModel::onSelectGoal,
          onSelectCalendarDate = { dateKey ->
            viewModel.onSelectCalendarDate(dateKey)
            viewModel.onScreenSelected(ScreenDestination.CALENDAR)
          },
          onToggleGeneralTask = viewModel::toggleGeneralTask,
          onToggleGoalTask = viewModel::toggleGoalTask,
          onPostponeTask = viewModel::postponeGeneralTask,
          onPostponeGoalTask = viewModel::postponeGoalTask,
          onIncrementHabit = viewModel::incrementHabit,
          onToggleHabitBoolean = viewModel::toggleHabitBoolean
        )
      }
      else -> {
        PlaceholderScreen(
          destination = uiState.currentScreen,
          onNavigateBack = { viewModel.onScreenSelected(ScreenDestination.HOME) }
        )
      }
    }
  }
}
