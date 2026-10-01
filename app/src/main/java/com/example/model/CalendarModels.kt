package com.example.model

enum class CalendarViewMode(val titleArabic: String, val iconText: String) {
  MONTH("شهري", "🗓️"),
  WEEK("أسبوعي", "📅"),
  AGENDA("أجندة", "📋")
}

data class DayHabitRecord(
  val habitId: String,
  val title: String,
  val iconEmoji: String,
  val targetValue: Int,
  val actualValue: Int,
  val unit: String,
  val type: HabitType,
  val isMandatory: Boolean,
  val isCompleted: Boolean,
  val statusNote: String? = null
) {
  val progressPercentage: Int
    get() = if (targetValue > 0) ((actualValue.toFloat() / targetValue) * 100).toInt().coerceIn(0, 100) else 0
}

data class DayTaskRecord(
  val task: Task,
  val isGoalTask: Boolean = false,
  val goalId: String? = null,
  val goalTitle: String? = null,
  val goalIconId: String? = null
)

data class DaySummaryHistory(
  val dateKey: String, // "yyyy/MM/dd"
  val dateDisplayArabic: String,
  val dayOfWeekArabic: DayOfWeekArabic,
  val habits: List<DayHabitRecord> = emptyList(),
  val tasks: List<DayTaskRecord> = emptyList(),
  val isToday: Boolean = false,
  val isPast: Boolean = false,
  val isFuture: Boolean = false
) {
  val totalItemsCount: Int
    get() = habits.size + tasks.size

  val completedHabitsCount: Int
    get() = habits.count { it.isCompleted }

  val completedTasksCount: Int
    get() = tasks.count { it.task.isCompleted }

  val completedItemsCount: Int
    get() = completedHabitsCount + completedTasksCount

  val postponedTasksCount: Int
    get() = tasks.count { it.task.isPostponed }

  val missedHabitsCount: Int
    get() = if (isPast) habits.count { !it.isCompleted } else 0

  val missedTasksCount: Int
    get() = if (isPast) tasks.count { !it.task.isCompleted && !it.task.isPostponed } else 0

  val missedItemsCount: Int
    get() = missedHabitsCount + missedTasksCount

  val completionRatePercentage: Int
    get() = if (totalItemsCount > 0) {
      ((completedItemsCount.toFloat() / totalItemsCount) * 100).toInt().coerceIn(0, 100)
    } else 0
}
