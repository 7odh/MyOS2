package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MyOSRepository
import com.example.model.AmbientSoundType
import com.example.model.AnalyticsTimePeriod
import com.example.model.CalendarViewMode
import com.example.model.DailyAnalytics
import com.example.model.DayOfWeekArabic
import com.example.model.FocusAttachment
import com.example.model.FocusAttachType
import com.example.model.FocusTimerMode
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Note
import com.example.model.Priority
import com.example.model.QuickAddType
import com.example.model.ScreenDestination
import com.example.model.SearchCategory
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.model.buildAnalyticsReport
import com.example.util.AmbientSoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyOSViewModel(
  private val repository: MyOSRepository = MyOSRepository()
) : ViewModel() {

  private val soundManager = AmbientSoundManager()
  private var focusTimerJob: Job? = null

  private val _uiState = MutableStateFlow(MyOSUiState())
  val uiState: StateFlow<MyOSUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        repository.user,
        repository.habits,
        repository.generalTasks,
        repository.goals,
        repository.notes
      ) { user, habits, tasks, goals, notes ->
        val activeGoals = goals.filter { !it.isPaused }
        // Today's goals progress is calculated strictly from today's scheduled goal tasks
        val todayGoalTasks = activeGoals.flatMap { it.todayTasks }

        val todayDay = com.example.model.getCurrentDayOfWeekArabic()
        val todayHabits = if (user.isRestModeActive) {
          habits.filter { it.isScheduledForToday(todayDay) && it.isMandatory }
        } else {
          habits.filter { it.isScheduledForToday(todayDay) }
        }
        val todayGeneralTasks = tasks.filter { !it.isGoalTask && it.schedule == TaskSchedule.TODAY }

        val analytics = DailyAnalytics(
          habitsCompleted = todayHabits.count { it.isCompleted },
          habitsTotal = todayHabits.size,
          tasksCompleted = todayGeneralTasks.count { it.isCompleted },
          tasksTotal = todayGeneralTasks.size,
          goalsCompleted = todayGoalTasks.count { it.isCompleted },
          goalsTotal = todayGoalTasks.size
        )
        val selectedDate = _uiState.value.selectedCalendarDate
        val daySummary = repository.getDaySummary(selectedDate)

        val report = buildAnalyticsReport(
          period = _uiState.value.selectedAnalyticsPeriod,
          habits = habits,
          generalTasks = tasks,
          goals = goals,
          dailyHistory = repository.dailyHistory.value,
          focusMinutesToday = _uiState.value.focusTotalMinutesToday,
          focusSessionsToday = _uiState.value.focusSessionsCompletedToday,
          isRestModeActive = user.isRestModeActive
        )

        _uiState.update { current ->
          val updatedAttachment = if (current.focusAttachment.type == FocusAttachType.HABIT) {
            habits.find { it.id == current.focusAttachment.id }?.let { h ->
              current.focusAttachment.copy(
                currentMinutes = h.currentValue,
                targetMinutes = h.targetValue
              )
            } ?: current.focusAttachment
          } else current.focusAttachment

          current.copy(
            user = user,
            habits = habits,
            generalTasks = tasks,
            goals = goals,
            notes = notes,
            dailyAnalytics = analytics,
            selectedDaySummary = daySummary,
            focusAttachment = updatedAttachment,
            analyticsReport = report,
            dailyHistory = repository.dailyHistory.value
          )
        }
      }.collect {}
    }
  }

  fun onSearchQueryChanged(query: String) {
    _uiState.update { it.copy(globalSearchQuery = query) }
  }

  fun onSearchCategorySelected(category: SearchCategory) {
    _uiState.update { it.copy(activeSearchCategory = category) }
  }

  fun onClearSearchQuery() {
    _uiState.update { it.copy(globalSearchQuery = "") }
  }

  fun onExecuteSearch(query: String) {
    val trimmed = query.trim()
    if (trimmed.isNotEmpty()) {
      _uiState.update { current ->
        val updatedRecent = (listOf(trimmed) + current.recentSearches.filterNot { it.equals(trimmed, ignoreCase = true) }).take(8)
        current.copy(globalSearchQuery = trimmed, recentSearches = updatedRecent)
      }
    }
  }

  fun onClearRecentSearches() {
    _uiState.update { it.copy(recentSearches = emptyList()) }
  }

  fun setAnalyticsPeriod(period: AnalyticsTimePeriod) {
    val current = _uiState.value
    val report = buildAnalyticsReport(
      period = period,
      habits = current.habits,
      generalTasks = current.generalTasks,
      goals = current.goals,
      dailyHistory = repository.dailyHistory.value,
      focusMinutesToday = current.focusTotalMinutesToday,
      focusSessionsToday = current.focusSessionsCompletedToday,
      isRestModeActive = current.user.isRestModeActive
    )
    _uiState.update { it.copy(selectedAnalyticsPeriod = period, analyticsReport = report) }
  }

  fun onScreenSelected(destination: ScreenDestination) {
    _uiState.update { it.copy(currentScreen = destination, selectedGoalId = null) }
  }

  fun onSelectGoal(goalId: String) {
    _uiState.update { it.copy(selectedGoalId = goalId) }
  }

  fun onClearSelectedGoal() {
    _uiState.update { it.copy(selectedGoalId = null) }
  }

  fun setGoalFilter(filter: GoalFilter) {
    _uiState.update { it.copy(activeGoalFilter = filter) }
  }

  fun openCreateGoalSheet() {
    _uiState.update { it.copy(isCreateGoalSheetVisible = true, editingGoal = null) }
  }

  fun closeCreateGoalSheet() {
    _uiState.update { it.copy(isCreateGoalSheetVisible = false, editingGoal = null) }
  }

  fun openEditGoal(goal: Goal) {
    _uiState.update { it.copy(isCreateGoalSheetVisible = true, editingGoal = goal) }
  }

  fun saveGoal(
    title: String,
    description: String?,
    iconId: String,
    priority: Priority,
    dueDate: String?,
    category: String? = null
  ) {
    val editing = _uiState.value.editingGoal
    if (editing != null) {
      repository.updateGoal(
        goalId = editing.id,
        title = title,
        description = description,
        iconId = iconId,
        priority = priority,
        dueDate = dueDate
      )
      _uiState.update { it.copy(notificationMessage = "تم تحديث الهدف بنجاح 🎯") }
    } else {
      repository.addGoal(
        title = title,
        description = description,
        iconId = iconId,
        priority = priority,
        dueDate = dueDate,
        category = category ?: "أهداف شخصية"
      )
      _uiState.update { it.copy(notificationMessage = "تم إنشاء الهدف الجديد بنجاح 🚀") }
    }
    closeCreateGoalSheet()
  }

  fun toggleGoalPause(goalId: String) {
    repository.toggleGoalPause(goalId)
    val goal = _uiState.value.goals.find { it.id == goalId }
    val wasPaused = goal?.isPaused ?: false
    val message = if (!wasPaused) {
      "تم ركن الهدف مؤقتاً. تاريخك وإنجازاتك محفوظة دائماً 📦"
    } else {
      "تم استئناف الهدف بنجاح! عودة موفقة 🎯"
    }
    _uiState.update { it.copy(notificationMessage = message) }
  }

  fun deleteGoal(goalId: String) {
    repository.deleteGoal(goalId)
    _uiState.update {
      it.copy(
        selectedGoalId = if (it.selectedGoalId == goalId) null else it.selectedGoalId,
        notificationMessage = "تم حذف الهدف"
      )
    }
  }

  fun openAddGoalTaskDialog() {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = true, editingGoalTask = null) }
  }

  fun openEditGoalTask(task: Task) {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = true, editingGoalTask = task) }
  }

  fun closeAddGoalTaskDialog() {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = false, editingGoalTask = null) }
  }

  fun saveGoalTask(
    goalId: String,
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    val editing = _uiState.value.editingGoalTask
    if (editing != null) {
      repository.updateGoalTask(
        goalId = goalId,
        taskId = editing.id,
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تم تعديل المهمة بنجاح ✅") }
    } else {
      repository.addGoalTask(
        goalId = goalId,
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تمت إضافة المهمة للهدف بنجاح ✨") }
    }
    closeAddGoalTaskDialog()
  }

  fun deleteGoalTask(goalId: String, taskId: String) {
    repository.deleteGoalTask(goalId, taskId)
    _uiState.update { it.copy(notificationMessage = "تم حذف المهمة") }
  }

  fun onToggleRestMode() {
    repository.toggleRestMode()
    val isRest = repository.user.value.isRestModeActive
    val message = if (isRest) {
      "تم تفعيل وضع الراحة لهذا اليوم ✨ استرح بدون قلق، لن تتأثر سلاسل إنجازاتك."
    } else {
      "تم إيقاف وضع الراحة، عودة موفقة لروتينك اليومي!"
    }
    _uiState.update { it.copy(notificationMessage = message) }
  }

  fun dismissNotification() {
    _uiState.update { it.copy(notificationMessage = null) }
  }

  fun openEditNameDialog() {
    _uiState.update { it.copy(isEditNameDialogVisible = true) }
  }

  fun closeEditNameDialog() {
    _uiState.update { it.copy(isEditNameDialogVisible = false) }
  }

  fun saveUserName(newName: String) {
    repository.updateUserName(newName)
    closeEditNameDialog()
  }

  fun openQuickAddSheet() {
    _uiState.update { it.copy(isQuickAddSheetVisible = true) }
  }

  fun closeQuickAddSheet() {
    _uiState.update { it.copy(isQuickAddSheetVisible = false) }
  }

  fun onQuickAddOptionSelected(type: QuickAddType) {
    _uiState.update { it.copy(isQuickAddSheetVisible = false) }
    when (type) {
      QuickAddType.GOAL -> openCreateGoalSheet()
      QuickAddType.HABIT -> openCreateHabitSheet()
      QuickAddType.TASK -> openCreateTaskSheet()
      else -> _uiState.update { it.copy(activeCreationDialog = type) }
    }
  }

  fun closeCreationDialog() {
    _uiState.update { it.copy(activeCreationDialog = null) }
  }

  fun setHabitFilter(filter: HabitFilter) {
    _uiState.update { it.copy(activeHabitFilter = filter) }
  }

  fun openCreateHabitSheet() {
    _uiState.update { it.copy(isCreateHabitSheetVisible = true, editingHabit = null) }
  }

  fun openEditHabit(habit: Habit) {
    _uiState.update { it.copy(isCreateHabitSheetVisible = true, editingHabit = habit) }
  }

  fun closeCreateHabitSheet() {
    _uiState.update { it.copy(isCreateHabitSheetVisible = false, editingHabit = null) }
  }

  fun saveHabit(
    title: String,
    type: HabitType,
    targetValue: Int,
    unit: String,
    frequency: HabitFrequency,
    scheduledDays: List<DayOfWeekArabic>,
    isMandatory: Boolean,
    priority: Priority,
    iconEmoji: String
  ) {
    val currentEditing = _uiState.value.editingHabit
    if (currentEditing != null) {
      repository.updateHabit(
        habitId = currentEditing.id,
        title = title,
        type = type,
        targetValue = targetValue,
        unit = unit,
        frequency = frequency,
        scheduledDays = scheduledDays,
        isMandatory = isMandatory,
        priority = priority,
        iconEmoji = iconEmoji
      )
      _uiState.update { it.copy(notificationMessage = "تم تعديل العادة بنجاح ✨") }
    } else {
      repository.addHabit(
        title = title,
        type = type,
        targetValue = targetValue,
        unit = unit,
        frequency = frequency,
        scheduledDays = scheduledDays,
        isMandatory = isMandatory,
        priority = priority,
        iconEmoji = iconEmoji
      )
      _uiState.update { it.copy(notificationMessage = "تمت إضافة العادة بنجاح 🌱") }
    }
    closeCreateHabitSheet()
  }

  fun deleteHabit(habitId: String) {
    repository.deleteHabit(habitId)
    _uiState.update { it.copy(notificationMessage = "تم حذف العادة بنجاح 🗑️") }
  }

  fun openPauseHabitDialog(habit: Habit) {
    _uiState.update { it.copy(selectedHabitToPause = habit) }
  }

  fun closePauseHabitDialog() {
    _uiState.update { it.copy(selectedHabitToPause = null) }
  }

  fun pauseHabit(habitId: String, durationDays: Int?) {
    repository.pauseHabit(habitId, durationDays)
    closePauseHabitDialog()
    val daysText = when (durationDays) {
      null -> "حتى الاستئناف يدوياً"
      1 -> "ليوم واحد"
      3 -> "لمدة 3 أيام"
      7 -> "لمدة أسبوع"
      14 -> "لمدة أسبوعين"
      30 -> "لمدة شهر"
      else -> "لمدة $durationDays يوماً"
    }
    _uiState.update { it.copy(notificationMessage = "تم ركن العادة مؤقتاً ($daysText). سجلك وسلسلتك محفوظة بأمان ⏸️") }
  }

  fun resumeHabit(habitId: String) {
    repository.resumeHabit(habitId)
    _uiState.update { it.copy(notificationMessage = "تم استئناف العادة بنجاح! عودة موفقة 🚀") }
  }

  fun toggleHabit(habitId: String) {
    repository.toggleHabit(habitId)
  }

  fun openLogHabitDialog(habit: com.example.model.Habit) {
    _uiState.update { it.copy(selectedHabitToLog = habit) }
  }

  fun closeLogHabitDialog() {
    _uiState.update { it.copy(selectedHabitToLog = null) }
  }

  fun incrementHabit(habitId: String) {
    repository.incrementHabit(habitId)
  }

  fun updateHabitProgress(habitId: String, newTotalValue: Int) {
    repository.updateHabitProgress(habitId, newTotalValue)
    closeLogHabitDialog()
  }

  fun toggleHabitBoolean(habitId: String) {
    repository.toggleHabitBoolean(habitId)
  }

  fun toggleGeneralTask(taskId: String) {
    repository.toggleGeneralTask(taskId)
  }

  fun toggleGoalTask(goalId: String, taskId: String) {
    repository.toggleGoalTask(goalId, taskId)
  }

  fun addHabit(title: String, priority: Priority) {
    repository.addHabit(title = title, priority = priority)
    closeCreationDialog()
    _uiState.update { it.copy(notificationMessage = "تمت إضافة العادة بنجاح 🌱") }
  }

  fun addTask(title: String, priority: Priority) {
    repository.addTask(title, priority)
    closeCreationDialog()
    _uiState.update { it.copy(notificationMessage = "تمت إضافة المهمة بنجاح ✅") }
  }

  fun setTaskFilter(filter: TaskFilter) {
    _uiState.update { it.copy(activeTaskFilter = filter) }
  }

  fun setTaskSortOrder(order: TaskSortOrder) {
    _uiState.update { it.copy(taskSortOrder = order) }
  }

  fun openCreateTaskSheet() {
    _uiState.update { it.copy(isCreateTaskSheetVisible = true, editingGeneralTask = null) }
  }

  fun openEditTask(task: Task) {
    _uiState.update { it.copy(isCreateTaskSheetVisible = true, editingGeneralTask = task) }
  }

  fun closeCreateTaskSheet() {
    _uiState.update { it.copy(isCreateTaskSheetVisible = false, editingGeneralTask = null) }
  }

  fun saveGeneralTask(
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    val editing = _uiState.value.editingGeneralTask
    if (editing != null) {
      repository.updateGeneralTask(
        taskId = editing.id,
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تم تعديل المهمة بنجاح ✅") }
    } else {
      repository.addGeneralTask(
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تمت إضافة المهمة بنجاح ✨") }
    }
    closeCreateTaskSheet()
  }

  fun deleteGeneralTask(taskId: String) {
    repository.deleteGeneralTask(taskId)
    _uiState.update { it.copy(notificationMessage = "تم حذف المهمة") }
  }

  fun postponeGeneralTask(taskId: String) {
    repository.postponeGeneralTask(taskId)
    val selectedDate = _uiState.value.selectedCalendarDate
    val updatedSummary = repository.getDaySummary(selectedDate)
    _uiState.update {
      it.copy(
        selectedDaySummary = updatedSummary,
        notificationMessage = "تم ترحيل المهمة إلى الغد بنجاح ➡️"
      )
    }
  }

  fun postponeGoalTask(goalId: String, taskId: String) {
    repository.postponeGoalTask(goalId, taskId)
    val selectedDate = _uiState.value.selectedCalendarDate
    val updatedSummary = repository.getDaySummary(selectedDate)
    _uiState.update {
      it.copy(
        selectedDaySummary = updatedSummary,
        notificationMessage = "تم ترحيل مهمة الهدف إلى الغد بنجاح ➡️"
      )
    }
  }

  fun onSelectCalendarDate(dateKey: String) {
    val summary = repository.getDaySummary(dateKey)
    _uiState.update {
      it.copy(
        selectedCalendarDate = dateKey,
        selectedDaySummary = summary
      )
    }
  }

  fun onChangeCalendarViewMode(mode: CalendarViewMode) {
    _uiState.update { it.copy(calendarViewMode = mode) }
  }

  fun openCreateNoteSheet() {
    _uiState.update { it.copy(isCreateNoteSheetVisible = true, editingNote = null) }
  }

  fun openEditNote(note: Note) {
    _uiState.update { it.copy(isCreateNoteSheetVisible = true, editingNote = note) }
  }

  fun closeCreateNoteSheet() {
    _uiState.update { it.copy(isCreateNoteSheetVisible = false, editingNote = null) }
  }

  fun saveNote(
    title: String,
    content: String,
    colorLong: Long,
    tag: String,
    isPinned: Boolean
  ) {
    val editing = _uiState.value.editingNote
    if (editing != null) {
      repository.updateNote(
        noteId = editing.id,
        title = title,
        content = content,
        colorLong = colorLong,
        tag = tag,
        isPinned = isPinned
      )
      _uiState.update { it.copy(notificationMessage = "تم تحديث الفكرة في المخزن 💡") }
    } else {
      repository.addNote(
        title = title,
        content = content,
        colorLong = colorLong,
        tag = tag,
        isPinned = isPinned
      )
      _uiState.update { it.copy(notificationMessage = "تم حفظ الفكرة في مخزن الأفكار ✨") }
    }
    closeCreateNoteSheet()
  }

  fun togglePinNote(noteId: String) {
    repository.togglePinNote(noteId)
  }

  fun deleteNote(noteId: String) {
    repository.deleteNote(noteId)
    _uiState.update { it.copy(notificationMessage = "تم حذف الفكرة") }
  }

  fun setActiveNoteTag(tag: String) {
    _uiState.update { it.copy(activeNoteTag = tag) }
  }

  fun setNoteSearchQuery(query: String) {
    _uiState.update { it.copy(noteSearchQuery = query) }
  }

  // Convert Idea to Goal
  fun convertNoteToGoal(note: Note) {
    val tempGoal = Goal(
      id = "",
      title = note.title,
      description = note.content,
      priority = Priority.HIGH
    )
    _uiState.update {
      it.copy(
        isCreateGoalSheetVisible = true,
        editingGoal = tempGoal,
        notificationMessage = "تحويل الفكرة إلى هدف 🎯"
      )
    }
  }

  // Convert Idea to Habit
  fun convertNoteToHabit(note: Note) {
    val tempHabit = Habit(
      id = "",
      title = note.title,
      priority = Priority.HIGH
    )
    _uiState.update {
      it.copy(
        isCreateHabitSheetVisible = true,
        editingHabit = tempHabit,
        notificationMessage = "تحويل الفكرة إلى عادة مقترحة 🌱"
      )
    }
  }

  // Convert Idea to Task
  fun convertNoteToTask(note: Note) {
    val tempTask = Task(
      id = "",
      title = note.title,
      notes = note.content,
      priority = Priority.MEDIUM,
      schedule = TaskSchedule.TODAY
    )
    _uiState.update {
      it.copy(
        isCreateTaskSheetVisible = true,
        editingGeneralTask = tempTask,
        notificationMessage = "تحويل الفكرة إلى مهمة ⚡"
      )
    }
  }

  // ── Focus & Pomodoro Functions ──

  fun setFocusMode(mode: FocusTimerMode) {
    focusTimerJob?.cancel()
    soundManager.stopSound()
    val seconds = mode.defaultMinutes * 60
    _uiState.update {
      it.copy(
        focusTimerMode = mode,
        focusTotalSeconds = seconds,
        focusRemainingSeconds = seconds,
        isFocusTimerRunning = false
      )
    }
  }

  fun setFocusDurationMinutes(minutes: Int) {
    focusTimerJob?.cancel()
    soundManager.stopSound()
    val validMinutes = minutes.coerceIn(1, 180)
    val seconds = validMinutes * 60
    val mode = when (validMinutes) {
      25 -> FocusTimerMode.POMODORO
      5 -> FocusTimerMode.SHORT_BREAK
      15 -> FocusTimerMode.LONG_BREAK
      else -> FocusTimerMode.CUSTOM
    }
    _uiState.update {
      it.copy(
        focusTimerMode = mode,
        focusTotalSeconds = seconds,
        focusRemainingSeconds = seconds,
        isFocusTimerRunning = false,
        isCustomDurationDialogVisible = false
      )
    }
  }

  fun startFocusTimer() {
    if (_uiState.value.isFocusTimerRunning) return
    if (_uiState.value.focusRemainingSeconds <= 0) {
      val resetSecs = _uiState.value.focusTotalSeconds
      _uiState.update { it.copy(focusRemainingSeconds = resetSecs) }
    }
    _uiState.update { it.copy(isFocusTimerRunning = true) }

    if (_uiState.value.selectedAmbientSound != AmbientSoundType.NONE) {
      soundManager.playSound(_uiState.value.selectedAmbientSound)
    }

    focusTimerJob?.cancel()
    focusTimerJob = viewModelScope.launch {
      while (_uiState.value.isFocusTimerRunning && _uiState.value.focusRemainingSeconds > 0) {
        delay(1000L)
        val currentRemaining = _uiState.value.focusRemainingSeconds
        if (currentRemaining <= 1) {
          _uiState.update { it.copy(focusRemainingSeconds = 0) }
          completeFocusSession(forceCompleted = true)
          break
        } else {
          _uiState.update { it.copy(focusRemainingSeconds = currentRemaining - 1) }
        }
      }
    }
  }

  fun pauseFocusTimer() {
    focusTimerJob?.cancel()
    soundManager.stopSound()
    _uiState.update { it.copy(isFocusTimerRunning = false) }
  }

  fun resetFocusTimer() {
    focusTimerJob?.cancel()
    soundManager.stopSound()
    _uiState.update {
      it.copy(
        isFocusTimerRunning = false,
        focusRemainingSeconds = it.focusTotalSeconds
      )
    }
  }

  fun finishEarlyAndRecordProgress() {
    completeFocusSession(forceCompleted = false)
  }

  private fun completeFocusSession(forceCompleted: Boolean) {
    focusTimerJob?.cancel()
    soundManager.stopSound()

    val totalSecs = _uiState.value.focusTotalSeconds
    val remainingSecs = _uiState.value.focusRemainingSeconds
    val elapsedSecs = totalSecs - remainingSecs
    val elapsedMinutes = if (forceCompleted || remainingSecs <= 0 || elapsedSecs <= 0) {
      (totalSecs / 60).coerceAtLeast(1)
    } else {
      (elapsedSecs / 60).coerceAtLeast(1)
    }

    val attachment = _uiState.value.focusAttachment
    var customNotification: String? = null

    when (attachment.type) {
      FocusAttachType.HABIT -> {
        val habit = repository.habits.value.find { it.id == attachment.id }
        if (habit != null) {
          val newCurrent = habit.currentValue + elapsedMinutes
          repository.updateHabitProgress(habit.id, newCurrent)

          customNotification = if (habit.targetValue > 0 && newCurrent > habit.targetValue) {
            "رائع جداً وبطل حقيقي! 🚀 تجاوزت المطلوب في عادة '${habit.title}' ($newCurrent من ${habit.targetValue} ${habit.unit})! شغف وعزيمة استثنائية! 🔥"
          } else if (habit.targetValue > 0 && newCurrent >= habit.targetValue) {
            "ألف مبروك! 🎉 أتممت المطلوب لليوم كاملاً في عادة '${habit.title}' ($newCurrent من ${habit.targetValue} ${habit.unit})! 🌟"
          } else {
            "تم تسجيل $elapsedMinutes دقيقة بنجاح لعادة '${habit.title}' ($newCurrent من ${habit.targetValue} ${habit.unit})! استمر! 👏"
          }
        }
      }
      FocusAttachType.GENERAL_TASK -> {
        val task = repository.generalTasks.value.find { it.id == attachment.id }
        if (task != null) {
          if (!task.isCompleted) {
            repository.toggleGeneralTask(task.id)
            customNotification = "عاش يا بطل! 🎯 تم إتمام مهمة '${task.title}' بعد جلسة تركيز $elapsedMinutes دقيقة! ✨"
          } else {
            customNotification = "ممتاز! 🎯 تم تسجيل جلسة تركيز مدتها $elapsedMinutes دقيقة لمهمة '${task.title}'!"
          }
        }
      }
      FocusAttachType.GOAL_TASK -> {
        val goalId = attachment.goalId
        if (goalId != null) {
          val goal = repository.goals.value.find { it.id == goalId }
          val task = goal?.tasks?.find { it.id == attachment.id }
          if (task != null && !task.isCompleted) {
            repository.toggleGoalTask(goalId, task.id)
            customNotification = "إنجاز مبهر! 🎯 تم إتمام مهمة '${task.title}' لهدف '${goal.title}' بعد جلسة $elapsedMinutes دقيقة! 🚀"
          } else {
            customNotification = "أحسنت! 🎯 تم إنجاز جلسة تركيز $elapsedMinutes دقيقة لمهام الهدف!"
          }
        }
      }
      FocusAttachType.NONE -> {
        customNotification = "عاش يا بطل! 🌟 أتممت جلسة تركيز عميقة بنجاح ($elapsedMinutes دقيقة)! استمر في هذا الزخم! 🔥"
      }
    }

    _uiState.update { current ->
      current.copy(
        isFocusTimerRunning = false,
        focusRemainingSeconds = current.focusTotalSeconds,
        focusSessionsCompletedToday = current.focusSessionsCompletedToday + 1,
        focusTotalMinutesToday = current.focusTotalMinutesToday + elapsedMinutes,
        notificationMessage = customNotification
      )
    }
  }

  fun setFocusAttachment(attachment: FocusAttachment) {
    _uiState.update {
      it.copy(
        focusAttachment = attachment,
        isAttachPickerVisible = false,
        notificationMessage = "تم ربط الجلسة بـ: ${attachment.title} 🔗"
      )
    }
  }

  fun clearFocusAttachment() {
    _uiState.update {
      it.copy(
        focusAttachment = FocusAttachment(
          type = FocusAttachType.NONE,
          title = "جلسة تركيز حرة",
          iconEmoji = "🎯"
        ),
        notificationMessage = "تم فك الارتباط، الجلسة الآن حرة 🎯"
      )
    }
  }

  fun setAmbientSound(soundType: AmbientSoundType) {
    _uiState.update { it.copy(selectedAmbientSound = soundType) }
    if (_uiState.value.isFocusTimerRunning) {
      soundManager.playSound(soundType)
    }
  }

  fun toggleAttachPicker(visible: Boolean) {
    _uiState.update { it.copy(isAttachPickerVisible = visible) }
  }

  fun toggleCustomDurationDialog(visible: Boolean) {
    _uiState.update { it.copy(isCustomDurationDialogVisible = visible) }
  }

  fun toggleFlipClockFullScreen(fullscreen: Boolean) {
    _uiState.update { it.copy(isFlipClockFullScreen = fullscreen) }
  }

  fun setThemeMode(mode: com.example.model.ThemeMode) {
    val updatedUser = _uiState.value.user.copy(themeMode = mode)
    repository.updateUser(updatedUser)
    _uiState.update { it.copy(user = updatedUser) }
  }

  fun toggleThemeMode() {
    val currentMode = _uiState.value.user.themeMode
    val nextMode = when (currentMode) {
      com.example.model.ThemeMode.SYSTEM -> com.example.model.ThemeMode.DARK
      com.example.model.ThemeMode.DARK -> com.example.model.ThemeMode.LIGHT
      com.example.model.ThemeMode.LIGHT -> com.example.model.ThemeMode.SYSTEM
    }
    setThemeMode(nextMode)
  }

  override fun onCleared() {
    super.onCleared()
    focusTimerJob?.cancel()
    soundManager.stopSound()
  }
}
