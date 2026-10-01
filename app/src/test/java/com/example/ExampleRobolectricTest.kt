package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MyOSRepository
import com.example.model.AmbientSoundProvider
import com.example.model.AmbientSoundType
import com.example.model.AnalyticsTimePeriod
import com.example.model.DayOfWeekArabic
import com.example.model.FocusAttachment
import com.example.model.FocusAttachType
import com.example.model.FocusTimerMode
import com.example.model.GoalStatus
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.model.ScreenDestination
import com.example.model.SearchCategory
import com.example.model.SearchResultItem
import com.example.model.TaskSchedule
import com.example.model.buildAnalyticsReport
import com.example.model.performGlobalSearch
import com.example.viewmodel.MyOSViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MyOS", appName)
  }

  @Test
  fun `test goal task date logic and priority ordering`() {
    val repository = MyOSRepository()
    val goal1 = repository.goals.value.first { it.id == "g1" }

    // Goal 1 has 4 total tasks, 3 completed -> 75%
    assertEquals(4, goal1.totalTasksCount)
    assertEquals(3, goal1.completedTasksCount)
    assertEquals(75, goal1.progressPercentage)

    // On Home screen: ONLY tasks scheduled for TODAY appear!
    val todayTasks = goal1.todayTasks
    assertEquals(2, todayTasks.size)
    assertTrue(todayTasks.all { it.schedule == TaskSchedule.TODAY })

    // In Goal Details: all 4 tasks appear in sorted order (High -> Medium -> Low)
    val sortedAllTasks = goal1.sortedTasks
    assertEquals(4, sortedAllTasks.size)
    assertEquals(Priority.HIGH, sortedAllTasks[0].priority)
    assertEquals(Priority.MEDIUM, sortedAllTasks[1].priority)
    assertEquals(Priority.LOW, sortedAllTasks[2].priority)
    assertEquals(Priority.LOW, sortedAllTasks[3].priority)
  }

  @Test
  fun `test goal completion status and exclusion from home screen`() {
    val repository = MyOSRepository()
    val goal3 = repository.goals.value.first { it.id == "g3" }

    // Goal 3 has 5 of 5 tasks completed -> 100%
    assertEquals(5, goal3.totalTasksCount)
    assertEquals(5, goal3.completedTasksCount)
    assertEquals(100, goal3.progressPercentage)
    assertEquals(GoalStatus.COMPLETED, goal3.status)

    // On the Home screen: Completed goals (100%) and Paused goals are excluded from active daily list!
    val homeActiveGoals = repository.goals.value.filter { !it.isPaused && it.progressPercentage < 100 }
    assertFalse(homeActiveGoals.any { it.id == "g3" })
    assertTrue(homeActiveGoals.any { it.id == "g1" }) // Goal 1 is 75%, still active
    assertTrue(homeActiveGoals.any { it.id == "g2" }) // Goal 2 is 40%, still active

    // Now complete the last remaining task of Goal 1 -> reaches 100%
    repository.toggleGoalTask("g1", "gt1_4")
    val goal1After = repository.goals.value.first { it.id == "g1" }
    assertEquals(100, goal1After.progressPercentage)

    // Goal 1 now disappears from Home screen active goals!
    val homeActiveGoalsAfter = repository.goals.value.filter { !it.isPaused && it.progressPercentage < 100 }
    assertFalse(homeActiveGoalsAfter.any { it.id == "g1" })
  }

  @Test
  fun `test delete goal removes goal and its tasks`() {
    val repository = MyOSRepository()
    assertTrue(repository.goals.value.any { it.id == "g1" })

    // Delete goal g1
    repository.deleteGoal("g1")
    assertFalse(repository.goals.value.any { it.id == "g1" })
  }

  @Test
  fun `test pause and resume goal preserves progress`() {
    val repository = MyOSRepository()
    val goal1Before = repository.goals.value.first { it.id == "g1" }
    assertEquals(75, goal1Before.progressPercentage)
    assertEquals(false, goal1Before.isPaused)
    assertEquals(GoalStatus.ACTIVE, goal1Before.status)

    // Pause goal (ركن الهدف)
    repository.toggleGoalPause("g1")
    val goal1Paused = repository.goals.value.first { it.id == "g1" }
    assertEquals(true, goal1Paused.isPaused)
    assertEquals(GoalStatus.PAUSED, goal1Paused.status)
    // Progress remains 75%
    assertEquals(75, goal1Paused.progressPercentage)
    assertEquals(3, goal1Paused.completedTasksCount)

    // Resume goal (استئناف الهدف)
    repository.toggleGoalPause("g1")
    val goal1Resumed = repository.goals.value.first { it.id == "g1" }
    assertEquals(false, goal1Resumed.isPaused)
    assertEquals(GoalStatus.ACTIVE, goal1Resumed.status)
    assertEquals(75, goal1Resumed.progressPercentage)
  }

  @Test
  fun `test rich habit tracking and custom frequency`() {
    val repository = MyOSRepository()

    // 1. Counter habit (الصلوات الخمس): 3 of 5, mandatory, daily
    val prayerHabit = repository.habits.value.first { it.id == "h1" }
    assertEquals(HabitType.COUNTER, prayerHabit.type)
    assertEquals(3, prayerHabit.currentValue)
    assertEquals(5, prayerHabit.targetValue)
    assertEquals("صلوات", prayerHabit.unit)
    assertTrue(prayerHabit.isMandatory)
    assertFalse(prayerHabit.isCompleted)

    // Increment counter twice
    repository.incrementHabit("h1")
    repository.incrementHabit("h1")
    val prayerUpdated = repository.habits.value.first { it.id == "h1" }
    assertEquals(5, prayerUpdated.currentValue)
    assertTrue(prayerUpdated.isCompleted)

    // 2. Specific days habit (الجيم: سبت، اثنين، خميس)
    val gymHabit = repository.habits.value.first { it.id == "h2" }
    assertEquals(HabitFrequency.SPECIFIC_DAYS, gymHabit.frequency)
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.SATURDAY))
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.MONDAY))
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.THURSDAY))
    assertFalse(gymHabit.isScheduledForToday(DayOfWeekArabic.FRIDAY))
    assertFalse(gymHabit.isScheduledForToday(DayOfWeekArabic.SUNDAY))
    assertFalse(gymHabit.isMandatory)

    // 3. Quantity habit with custom unit (تمارين الضغط: عدات)
    val pushupsHabit = repository.habits.value.first { it.id == "h5" }
    assertEquals(HabitType.QUANTITY, pushupsHabit.type)
    assertEquals("عدة", pushupsHabit.unit)
    assertEquals(50, pushupsHabit.targetValue)
    assertEquals(30, pushupsHabit.currentValue)

    // 4. Boolean habit (أذكار الصباح والمساء)
    val azkarHabit = repository.habits.value.first { it.id == "h7" }
    assertEquals(HabitType.BOOLEAN, azkarHabit.type)
    assertTrue(azkarHabit.isCompleted)
    assertTrue(azkarHabit.isMandatory)

    repository.toggleHabitBoolean("h7")
    val azkarToggled = repository.habits.value.first { it.id == "h7" }
    assertFalse(azkarToggled.isCompleted)
  }

  @Test
  fun `test habit CRUD and custom unit creation`() {
    val repository = MyOSRepository()

    // Add new habit with custom unit
    repository.addHabit(
      title = "تمارين نط الحبل",
      type = HabitType.QUANTITY,
      targetValue = 200,
      unit = "نطة",
      frequency = HabitFrequency.DAILY,
      isMandatory = false,
      priority = Priority.MEDIUM,
      iconEmoji = "🪢"
    )

    val newHabit = repository.habits.value.first { it.title == "تمارين نط الحبل" }
    assertEquals("نطة", newHabit.unit)
    assertEquals(200, newHabit.targetValue)
    assertEquals(HabitType.QUANTITY, newHabit.type)

    // Update habit
    repository.updateHabit(
      habitId = newHabit.id,
      title = "تمارين نط الحبل المتقدمة",
      type = HabitType.QUANTITY,
      targetValue = 300,
      unit = "قفزة",
      frequency = HabitFrequency.DAILY,
      scheduledDays = emptyList(),
      isMandatory = true,
      priority = Priority.HIGH,
      iconEmoji = "🪢"
    )

    val updatedHabit = repository.habits.value.first { it.id == newHabit.id }
    assertEquals("تمارين نط الحبل المتقدمة", updatedHabit.title)
    assertEquals(300, updatedHabit.targetValue)
    assertEquals("قفزة", updatedHabit.unit)
    assertTrue(updatedHabit.isMandatory)

    // Delete habit
    repository.deleteHabit(newHabit.id)
    assertFalse(repository.habits.value.any { it.id == newHabit.id })
  }

  @Test
  fun `test rest mode affects non-mandatory habits`() {
    val repository = MyOSRepository()
    assertEquals(false, repository.user.value.isRestModeActive)

    val prayer = repository.habits.value.first { it.id == "h1" }
    val gym = repository.habits.value.first { it.id == "h2" }

    assertTrue(prayer.isMandatory) // Prayer is mandatory
    assertFalse(gym.isMandatory)   // Gym is non-mandatory

    // Toggle rest mode
    repository.toggleRestMode()
    assertTrue(repository.user.value.isRestModeActive)

    // In Rest Mode, analytics only expects mandatory habits for today
    val analytics = repository.calculateAnalytics()
    assertTrue(analytics.habitsTotal > 0)
    // Mandatory habits (prayer, Quran, water, Azkar) remain counted
    val mandatoryHabitsCount = repository.habits.value.count { it.isMandatory }
    assertEquals(mandatoryHabitsCount, analytics.habitsTotal)
  }

  @Test
  fun `test habit pause freeze feature with history preservation`() {
    val repository = MyOSRepository()
    val pushupsBefore = repository.habits.value.first { it.id == "h5" }
    assertEquals(11, pushupsBefore.currentStreak)
    assertEquals(30, pushupsBefore.currentValue)
    assertFalse(pushupsBefore.isCurrentlyPaused)

    // Pause pushups habit for 7 days (ركن العادة لمدة أسبوع)
    repository.pauseHabit("h5", durationDays = 7)
    val pushupsPaused = repository.habits.value.first { it.id == "h5" }

    // History and streak are fully preserved
    assertEquals(11, pushupsPaused.currentStreak)
    assertEquals(30, pushupsPaused.currentValue)
    assertTrue(pushupsPaused.isPaused)
    assertTrue(pushupsPaused.isCurrentlyPaused)
    assertEquals(7, pushupsPaused.pauseRemainingDays)
    assertTrue(pushupsPaused.pauseStatusDescription!!.contains("7 أيام") || pushupsPaused.pauseStatusDescription!!.contains("أيام"))

    // Paused habit must NEVER appear in today's schedule on Home screen
    val todayDay = com.example.model.getCurrentDayOfWeekArabic()
    assertFalse(pushupsPaused.isScheduledForToday(todayDay))

    // Resume the habit (استئناف العادة)
    repository.resumeHabit("h5")
    val pushupsResumed = repository.habits.value.first { it.id == "h5" }

    assertFalse(pushupsResumed.isPaused)
    assertFalse(pushupsResumed.isCurrentlyPaused)
    // Streak is still preserved
    assertEquals(11, pushupsResumed.currentStreak)
    assertEquals(30, pushupsResumed.currentValue)
    assertTrue(pushupsResumed.isScheduledForToday(todayDay))
  }

  @Test
  fun `test home screen only displays due today items and excludes no-date tasks`() {
    val repository = MyOSRepository()
    val allTasks = repository.generalTasks.value

    // Confirm we have tasks with TODAY, NO_DATE, and TOMORROW
    assertTrue(allTasks.any { it.schedule == TaskSchedule.TODAY })
    assertTrue(allTasks.any { it.schedule == TaskSchedule.NO_DATE })
    assertTrue(allTasks.any { it.schedule == TaskSchedule.TOMORROW })

    // On Home screen: ONLY general tasks with schedule == TODAY are displayed
    val homeTasks = allTasks.filter { !it.isGoalTask && it.schedule == TaskSchedule.TODAY }
    assertTrue(homeTasks.all { it.schedule == TaskSchedule.TODAY })
    assertFalse(homeTasks.any { it.schedule == TaskSchedule.NO_DATE })
    assertFalse(homeTasks.any { it.schedule == TaskSchedule.TOMORROW })

    // Analytics strictly counts only today's tasks
    val analytics = repository.calculateAnalytics()
    assertEquals(homeTasks.size, analytics.tasksTotal)
    assertEquals(homeTasks.count { it.isCompleted }, analytics.tasksCompleted)
  }

  @Test
  fun `test tasks screen priority ordering and without-date tasks`() {
    val repository = MyOSRepository()

    // Add a high priority task with no date
    repository.addGeneralTask(
      title = "فكرة مشروع جديدة",
      notes = "تجهيز المسودة",
      priority = Priority.HIGH,
      schedule = TaskSchedule.NO_DATE,
      dueDateFormatted = null
    )

    // Add a no-priority (NONE) task for today
    repository.addGeneralTask(
      title = "شراء خبز",
      notes = null,
      priority = Priority.NONE,
      schedule = TaskSchedule.TODAY,
      dueDateFormatted = null
    )

    val allTasks = repository.generalTasks.value
    val noDateTask = allTasks.first { it.title == "فكرة مشروع جديدة" }
    val nonePriorityTask = allTasks.first { it.title == "شراء خبز" }

    // 1. Task without date does NOT appear in Home screen (isDueToday is false)
    assertFalse(noDateTask.isDueToday)

    // 2. Task with TODAY schedule appears in Home screen
    assertTrue(nonePriorityTask.isDueToday)

    // 3. Sorting by priority orders HIGH (rank 1) before MEDIUM (rank 2) before LOW (rank 3) before NONE (rank 4)
    val sortedByPriority = allTasks.filter { !it.isCompleted }.sortedWith(
      compareBy<com.example.model.Task> { it.priority.rank }
    )
    val highIndex = sortedByPriority.indexOfFirst { it.priority == Priority.HIGH }
    val noneIndex = sortedByPriority.indexOfFirst { it.priority == Priority.NONE }
    assertTrue(highIndex < noneIndex)

    // 4. Update task to add a date
    repository.updateGeneralTask(
      taskId = noDateTask.id,
      title = "فكرة مشروع جديدة ومراجعة",
      notes = "تم تحديث المسودة",
      priority = Priority.HIGH,
      schedule = TaskSchedule.TODAY,
      dueDateFormatted = com.example.model.getTodayDateString()
    )

    val updatedTask = repository.generalTasks.value.first { it.id == noDateTask.id }
    assertEquals("فكرة مشروع جديدة ومراجعة", updatedTask.title)
    assertTrue(updatedTask.isDueToday)

    // 5. Delete task
    repository.deleteGeneralTask(updatedTask.id)
    assertFalse(repository.generalTasks.value.any { it.id == updatedTask.id })
  }

  @Test
  fun `test postpone task to tomorrow records postponement metadata`() {
    val repository = MyOSRepository()
    val todayTask = repository.generalTasks.value.first { it.id == "t1" }
    assertEquals(TaskSchedule.TODAY, todayTask.schedule)
    assertFalse(todayTask.isPostponed)
    assertEquals(0, todayTask.postponedCount)

    // Postpone task t1 to tomorrow
    repository.postponeGeneralTask("t1")

    val postponedTask = repository.generalTasks.value.first { it.id == "t1" }
    assertEquals(TaskSchedule.TOMORROW, postponedTask.schedule)
    assertTrue(postponedTask.isPostponed)
    assertEquals(1, postponedTask.postponedCount)
    assertEquals(com.example.model.getTodayDateString(), postponedTask.postponedFromDate)
    assertEquals(com.example.model.getTomorrowDateString(), postponedTask.dueDateFormatted)

    // Because it's now TOMORROW, it should no longer be due today on Home screen!
    assertFalse(postponedTask.isDueToday)
  }

  @Test
  fun `test postpone goal task to tomorrow`() {
    val repository = MyOSRepository()
    val goal = repository.goals.value.first { it.id == "g1" }
    val goalTask = goal.tasks.first { it.id == "gt1_1" }
    assertFalse(goalTask.isPostponed)

    // Postpone goal task gt1_1
    repository.postponeGoalTask("g1", "gt1_1")

    val updatedGoal = repository.goals.value.first { it.id == "g1" }
    val updatedGoalTask = updatedGoal.tasks.first { it.id == "gt1_1" }
    assertTrue(updatedGoalTask.isPostponed)
    assertEquals(TaskSchedule.TOMORROW, updatedGoalTask.schedule)
    assertEquals(com.example.model.getTomorrowDateString(), updatedGoalTask.dueDateFormatted)
  }

  @Test
  fun `test calendar history retrieval for past day with partial habit completion`() {
    val repository = MyOSRepository()
    val yestKey = com.example.data.getDateOffsetKey(-1)

    val yestSummary = repository.getDaySummary(yestKey)
    assertTrue(yestSummary.isPast)
    assertFalse(yestSummary.isToday)
    assertEquals(yestKey, yestSummary.dateKey)

    // Check specific habits requested: Reading 7/10, Gym 0/60 missed, Prayer 5/5 completed
    val quranHabit = yestSummary.habits.first { it.habitId == "h3" }
    assertEquals(7, quranHabit.actualValue)
    assertEquals(10, quranHabit.targetValue)
    assertFalse(quranHabit.isCompleted)
    assertEquals("صفحة", quranHabit.unit)

    val gymHabit = yestSummary.habits.first { it.habitId == "h2" }
    assertEquals(0, gymHabit.actualValue)
    assertEquals(60, gymHabit.targetValue)
    assertFalse(gymHabit.isCompleted)

    val prayerHabit = yestSummary.habits.first { it.habitId == "h1" }
    assertEquals(5, prayerHabit.actualValue)
    assertEquals(5, prayerHabit.targetValue)
    assertTrue(prayerHabit.isCompleted)

    // Verify completion rate percentage calculation
    assertTrue(yestSummary.completionRatePercentage in 1..99)
    assertTrue(yestSummary.missedItemsCount > 0)
    assertTrue(yestSummary.completedItemsCount > 0)
  }

  @Test
  fun `test calendar live summary for today`() {
    val repository = MyOSRepository()
    val todayKey = com.example.model.getTodayDateString()

    val todaySummary = repository.getDaySummary(todayKey)
    assertTrue(todaySummary.isToday)
    assertFalse(todaySummary.isPast)
    assertEquals(todayKey, todaySummary.dateKey)
    assertTrue(todaySummary.habits.isNotEmpty())
    assertTrue(todaySummary.tasks.isNotEmpty())
  }

  @Test
  fun `test idea inbox note CRUD and pinning`() {
    val repository = MyOSRepository()
    val initialNotesCount = repository.notes.value.size
    assertTrue(initialNotesCount > 0)

    // Add new idea note
    repository.addNote(
      title = "فكرة قناة بودكاست صوتية",
      content = "بودكاست أسبوعي يناقش كتب الإنتاجية وتجارب رواد الأعمال",
      colorLong = 0xFFEFF6FF,
      tag = "مشاريع مستقبلية",
      isPinned = false
    )

    val notesAfterAdd = repository.notes.value
    assertEquals(initialNotesCount + 1, notesAfterAdd.size)
    val addedNote = notesAfterAdd.first { it.title == "فكرة قناة بودكاست صوتية" }
    assertEquals("مشاريع مستقبلية", addedNote.tag)
    assertFalse(addedNote.isPinned)

    // Toggle Pin
    repository.togglePinNote(addedNote.id)
    val pinnedNote = repository.notes.value.first { it.id == addedNote.id }
    assertTrue(pinnedNote.isPinned)

    // Update Note
    repository.updateNote(
      noteId = addedNote.id,
      title = "فكرة بودكاست العقل الثاني",
      content = "تحديث الفكرة لتشمل استضافة خبراء",
      colorLong = 0xFFECFDF5,
      tag = "مشاريع مستقبلية",
      isPinned = true
    )
    val updatedNote = repository.notes.value.first { it.id == addedNote.id }
    assertEquals("فكرة بودكاست العقل الثاني", updatedNote.title)
    assertEquals(0xFFECFDF5, updatedNote.colorLong)

    // Delete Note
    repository.deleteNote(addedNote.id)
    assertEquals(initialNotesCount, repository.notes.value.size)
    assertFalse(repository.notes.value.any { it.id == addedNote.id })
  }

  @Test
  fun `test focus timer mode and duration settings`() {
    val repository = MyOSRepository()
    val viewModel = MyOSViewModel(repository)

    // Default mode is Pomodoro with 25 minutes
    assertEquals(FocusTimerMode.POMODORO, viewModel.uiState.value.focusTimerMode)
    assertEquals(25 * 60, viewModel.uiState.value.focusTotalSeconds)

    // Switch to Short Break (5 min)
    viewModel.setFocusMode(FocusTimerMode.SHORT_BREAK)
    assertEquals(FocusTimerMode.SHORT_BREAK, viewModel.uiState.value.focusTimerMode)
    assertEquals(5 * 60, viewModel.uiState.value.focusTotalSeconds)

    // Switch to Custom 45 minutes
    viewModel.setFocusDurationMinutes(45)
    assertEquals(FocusTimerMode.CUSTOM, viewModel.uiState.value.focusTimerMode)
    assertEquals(45 * 60, viewModel.uiState.value.focusTotalSeconds)
  }

  @Test
  fun `test focus auto-progress sync with timed habit and completion`() {
    val repository = MyOSRepository()
    val viewModel = MyOSViewModel(repository)

    // Habit h4: "التدرب على الكيبورد" (target 30 minutes, current 20 minutes)
    val keyboardHabitBefore = repository.habits.value.first { it.id == "h4" }
    assertEquals(30, keyboardHabitBefore.targetValue)
    assertEquals(20, keyboardHabitBefore.currentValue)
    assertFalse(keyboardHabitBefore.isCompleted)

    // Link focus session to keyboard habit
    viewModel.setFocusAttachment(
      FocusAttachment(
        type = FocusAttachType.HABIT,
        id = "h4",
        title = keyboardHabitBefore.title,
        iconEmoji = keyboardHabitBefore.iconEmoji,
        targetMinutes = keyboardHabitBefore.targetValue,
        currentMinutes = keyboardHabitBefore.currentValue,
        unit = keyboardHabitBefore.unit,
        isTimedHabit = true
      )
    )

    // Set a 10-minute session to complete the remaining 10 minutes (20 + 10 = 30)
    viewModel.setFocusDurationMinutes(10)
    viewModel.finishEarlyAndRecordProgress()

    // Habit h4 should now have 30 minutes and be marked completed!
    val keyboardHabitAfter = repository.habits.value.first { it.id == "h4" }
    assertEquals(30, keyboardHabitAfter.currentValue)
    assertTrue(keyboardHabitAfter.isCompleted)
    assertEquals(100, keyboardHabitAfter.progressPercentage)
  }

  @Test
  fun `test focus auto-progress exceeding habit target shows motivational message`() {
    val repository = MyOSRepository()
    val viewModel = MyOSViewModel(repository)

    val habit = repository.habits.value.first { it.id == "h4" }
    viewModel.setFocusAttachment(
      FocusAttachment(
        type = FocusAttachType.HABIT,
        id = habit.id,
        title = habit.title,
        iconEmoji = habit.iconEmoji,
        targetMinutes = habit.targetValue,
        currentMinutes = habit.currentValue,
        unit = habit.unit,
        isTimedHabit = true
      )
    )

    // Start with current 20, add 25 min -> total 45 min (> 30 min target)
    viewModel.setFocusDurationMinutes(25)
    viewModel.finishEarlyAndRecordProgress()

    val habitAfter = repository.habits.value.first { it.id == "h4" }
    assertEquals(45, habitAfter.currentValue)
    assertTrue(habitAfter.isCompleted)

    // Notification message should praise exceeding the target
    val notification = viewModel.uiState.value.notificationMessage ?: ""
    assertTrue(notification.contains("تجاوزت المطلوب") || notification.contains("بطل حقيقي"))
  }

  @Test
  fun `test ambient sound provider has required natural sounds`() {
    val sounds = AmbientSoundProvider.defaultSounds
    assertEquals(5, sounds.size)
    assertTrue(sounds.any { it.type == AmbientSoundType.RAIN })
    assertTrue(sounds.any { it.type == AmbientSoundType.OCEAN })
    assertTrue(sounds.any { it.type == AmbientSoundType.FIRE })
    assertTrue(sounds.any { it.type == AmbientSoundType.SNOW })
    assertTrue(sounds.any { it.type == AmbientSoundType.FOREST })
  }

  @Test
  fun `test analytics report builds for all four periods`() {
    val repository = MyOSRepository()
    val habits = repository.habits.value
    val tasks = repository.generalTasks.value
    val goals = repository.goals.value
    val history = repository.dailyHistory.value

    val todayReport = buildAnalyticsReport(AnalyticsTimePeriod.TODAY, habits, tasks, goals, history)
    assertEquals(AnalyticsTimePeriod.TODAY, todayReport.period)
    assertTrue(todayReport.productivityScore in 10..100)
    assertTrue(todayReport.chartBars.isNotEmpty())
    assertTrue(todayReport.insights.isNotEmpty())

    val weekReport = buildAnalyticsReport(AnalyticsTimePeriod.WEEK, habits, tasks, goals, history)
    assertEquals(AnalyticsTimePeriod.WEEK, weekReport.period)
    assertEquals(7, weekReport.chartBars.size)
    assertTrue(weekReport.overallCompletionRate > 0)

    val monthReport = buildAnalyticsReport(AnalyticsTimePeriod.MONTH, habits, tasks, goals, history)
    assertEquals(AnalyticsTimePeriod.MONTH, monthReport.period)
    assertEquals(4, monthReport.chartBars.size)

    val yearReport = buildAnalyticsReport(AnalyticsTimePeriod.YEAR, habits, tasks, goals, history)
    assertEquals(AnalyticsTimePeriod.YEAR, yearReport.period)
    assertEquals(9, yearReport.chartBars.size)
  }

  @Test
  fun `test analytics four pillars progress shortfall rest postponed calculations`() {
    val repository = MyOSRepository()
    val habits = repository.habits.value
    val tasks = repository.generalTasks.value
    val goals = repository.goals.value
    val history = repository.dailyHistory.value

    val weekReport = buildAnalyticsReport(AnalyticsTimePeriod.WEEK, habits, tasks, goals, history)

    // 1. Progress (التقدم)
    assertTrue(weekReport.totalCompletedItems > 0)
    assertTrue(weekReport.overallCompletionRate in 50..100)
    assertTrue(weekReport.habitsCompleted > 0)
    assertTrue(weekReport.focusTotalMinutes > 0)

    // 2. Shortfall (التقصير)
    assertTrue(weekReport.totalMissedItems >= 0)
    assertTrue(weekReport.topMissedHabits.isNotEmpty())
    assertTrue(weekReport.topMissedHabits.any { it.habitId == "h2" }) // Gym missed days

    // 3. Rest (الراحة)
    assertTrue(weekReport.restDaysCount > 0)
    assertFalse(weekReport.restBalanceStatus.isEmpty())

    // 4. Postponed (التأجيل)
    assertTrue(weekReport.postponedTasksCount > 0)
    assertTrue(weekReport.topPostponedTasks.isNotEmpty())
  }

  @Test
  fun `test viewmodel switches analytics period and updates report`() {
    val repository = MyOSRepository()
    val viewModel = MyOSViewModel(repository)

    // Default period is WEEK
    assertEquals(AnalyticsTimePeriod.WEEK, viewModel.uiState.value.selectedAnalyticsPeriod)

    // Switch to TODAY
    viewModel.setAnalyticsPeriod(AnalyticsTimePeriod.TODAY)
    assertEquals(AnalyticsTimePeriod.TODAY, viewModel.uiState.value.selectedAnalyticsPeriod)
    assertEquals(AnalyticsTimePeriod.TODAY, viewModel.uiState.value.analyticsReport.period)

    // Switch to MONTH
    viewModel.setAnalyticsPeriod(AnalyticsTimePeriod.MONTH)
    assertEquals(AnalyticsTimePeriod.MONTH, viewModel.uiState.value.selectedAnalyticsPeriod)
    assertEquals(AnalyticsTimePeriod.MONTH, viewModel.uiState.value.analyticsReport.period)

    // Switch to YEAR
    viewModel.setAnalyticsPeriod(AnalyticsTimePeriod.YEAR)
    assertEquals(AnalyticsTimePeriod.YEAR, viewModel.uiState.value.selectedAnalyticsPeriod)
    assertEquals(AnalyticsTimePeriod.YEAR, viewModel.uiState.value.analyticsReport.period)

    // Navigation to ANALYTICS destination
    viewModel.onScreenSelected(ScreenDestination.ANALYTICS)
    assertEquals(ScreenDestination.ANALYTICS, viewModel.uiState.value.currentScreen)
  }

  @Test
  fun `test global search recognizes tasks habits goals notes and calendar with arabic normalization`() {
    val repository = MyOSRepository()
    val habits = repository.habits.value
    val tasks = repository.generalTasks.value
    val goals = repository.goals.value
    val notes = repository.notes.value
    val history = repository.dailyHistory.value

    // Search with Arabic normalization (alef without hamza: "قران" matches "الورد القرآني")
    val quranResults = performGlobalSearch("قران", SearchCategory.ALL, habits, tasks, goals, notes, history)
    assertTrue(quranResults.isNotEmpty())
    assertTrue(quranResults.any { it is SearchResultItem.HabitItem && it.habit.id == "h3" })

    // Search for "جيم" matches gym habit
    val gymResults = performGlobalSearch("جيم", SearchCategory.ALL, habits, tasks, goals, notes, history)
    assertTrue(gymResults.any { it is SearchResultItem.HabitItem && it.habit.id == "h2" })

    // Search for goal: "انجليزي" matches "إتقان اللغة الإنجليزية"
    val englishResults = performGlobalSearch("انجليزي", SearchCategory.ALL, habits, tasks, goals, notes, history)
    assertTrue(englishResults.any { it is SearchResultItem.GoalItem && it.goal.id == "g1" })

    // Search for task: "تصميم" matches "تصميم واجهة لوحة التحكم"
    val designResults = performGlobalSearch("تصميم", SearchCategory.ALL, habits, tasks, goals, notes, history)
    assertTrue(designResults.any { it is SearchResultItem.TaskItem })

    // Search category filtering: SearchCategory.HABITS returns only habits
    val habitsOnlyResults = performGlobalSearch("صلاة", SearchCategory.HABITS, habits, tasks, goals, notes, history)
    assertTrue(habitsOnlyResults.all { it is SearchResultItem.HabitItem })
  }

  @Test
  fun `test viewmodel handles global search query and recent history`() {
    val repository = MyOSRepository()
    val viewModel = MyOSViewModel(repository)

    // Initially query is empty and has default recent searches
    assertEquals("", viewModel.uiState.value.globalSearchQuery)
    assertTrue(viewModel.uiState.value.recentSearches.isNotEmpty())

    // Update search query
    viewModel.onSearchQueryChanged("كود")
    assertEquals("كود", viewModel.uiState.value.globalSearchQuery)
    assertTrue(viewModel.uiState.value.searchResults.isNotEmpty())

    // Filter by GOALS
    viewModel.onSearchCategorySelected(SearchCategory.GOALS)
    assertEquals(SearchCategory.GOALS, viewModel.uiState.value.activeSearchCategory)
    assertTrue(viewModel.uiState.value.searchResults.all { it is SearchResultItem.GoalItem })

    // Execute search saves query to recent searches
    viewModel.onExecuteSearch("تطبيق")
    assertTrue(viewModel.uiState.value.recentSearches.contains("تطبيق"))

    // Clear search
    viewModel.onClearSearchQuery()
    assertEquals("", viewModel.uiState.value.globalSearchQuery)

    // Clear recent searches
    viewModel.onClearRecentSearches()
    assertTrue(viewModel.uiState.value.recentSearches.isEmpty())

    // Navigation to SEARCH screen
    viewModel.onScreenSelected(ScreenDestination.SEARCH)
    assertEquals(ScreenDestination.SEARCH, viewModel.uiState.value.currentScreen)
  }
}
