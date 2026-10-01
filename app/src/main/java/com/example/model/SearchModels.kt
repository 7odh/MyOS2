package com.example.model

enum class SearchCategory(
  val titleArabic: String,
  val iconEmoji: String
) {
  ALL("الكل", "🌐"),
  TASKS("المهام", "⚡"),
  HABITS("العادات", "🌱"),
  GOALS("الأهداف", "🎯"),
  NOTES("الملاحظات", "💡"),
  CALENDAR("التقويم", "📅")
}

sealed class SearchResultItem {
  abstract val id: String
  abstract val title: String
  abstract val subtitle: String?
  abstract val category: SearchCategory
  abstract val iconText: String

  data class TaskItem(
    val task: Task,
    val isGoalTask: Boolean = false,
    val goalTitle: String? = null,
    val goalId: String? = null
  ) : SearchResultItem() {
    override val id: String = task.id
    override val title: String = task.title
    override val subtitle: String? = when {
      !task.notes.isNullOrBlank() -> task.notes
      isGoalTask && !goalTitle.isNullOrBlank() -> "تابع لهدف: $goalTitle"
      task.isPostponed -> "مُرحّلة للغد ➡️"
      task.dueDateFormatted != null -> "الموعد: ${task.dueDateFormatted}"
      else -> "مهمة عامة"
    }
    override val category: SearchCategory = SearchCategory.TASKS
    override val iconText: String = if (task.isCompleted) "✅" else "⚡"
  }

  data class HabitItem(
    val habit: Habit
  ) : SearchResultItem() {
    override val id: String = habit.id
    override val title: String = habit.title
    override val subtitle: String = "${habit.currentValue} من ${habit.targetValue} ${habit.unit} • Streak: ${habit.currentStreak} يوم 🔥"
    override val category: SearchCategory = SearchCategory.HABITS
    override val iconText: String = habit.iconEmoji
  }

  data class GoalItem(
    val goal: Goal
  ) : SearchResultItem() {
    override val id: String = goal.id
    override val title: String = goal.title
    override val subtitle: String? = when {
      !goal.description.isNullOrBlank() -> goal.description
      else -> "${goal.completedTasksCount} من ${goal.totalTasksCount} خطوات منجزة (${goal.progressPercentage}%)"
    }
    override val category: SearchCategory = SearchCategory.GOALS
    override val iconText: String = goal.iconEmoji
  }

  data class NoteItem(
    val note: Note
  ) : SearchResultItem() {
    override val id: String = note.id
    override val title: String = note.title
    override val subtitle: String = note.content
    override val category: SearchCategory = SearchCategory.NOTES
    override val iconText: String = "💡"
  }

  data class CalendarItem(
    val dateKey: String,
    val dateDisplayArabic: String,
    val summary: DaySummaryHistory
  ) : SearchResultItem() {
    override val id: String = dateKey
    override val title: String = dateDisplayArabic
    override val subtitle: String = "${summary.completedItemsCount} من ${summary.totalItemsCount} أنشطة منجزة (${summary.completionRatePercentage}%)"
    override val category: SearchCategory = SearchCategory.CALENDAR
    override val iconText: String = "📅"
  }
}

/**
 * Normalizes Arabic text for flexible, tolerant searching
 * (e.g. أ, إ, آ -> ا; ة -> ه; ى -> ي)
 */
fun normalizeArabic(text: String): String {
  return text.trim().lowercase()
    .replace('أ', 'ا')
    .replace('إ', 'ا')
    .replace('آ', 'ا')
    .replace('ة', 'ه')
    .replace('ى', 'ي')
    .replace("ـ", "") // Tatweel
}

fun performGlobalSearch(
  query: String,
  category: SearchCategory,
  habits: List<Habit>,
  generalTasks: List<Task>,
  goals: List<Goal>,
  notes: List<Note>,
  dailyHistory: Map<String, DaySummaryHistory>
): List<SearchResultItem> {
  val cleanQuery = normalizeArabic(query)
  if (cleanQuery.isBlank()) return emptyList()

  val results = mutableListOf<SearchResultItem>()

  // 1. Search Tasks (General & Goal tasks)
  if (category == SearchCategory.ALL || category == SearchCategory.TASKS) {
    generalTasks.forEach { task ->
      val titleNorm = normalizeArabic(task.title)
      val notesNorm = normalizeArabic(task.notes ?: "")
      val scheduleNorm = normalizeArabic(task.schedule.name)
      val priorityNorm = normalizeArabic(task.priority.name)

      if (titleNorm.contains(cleanQuery) || notesNorm.contains(cleanQuery) || scheduleNorm.contains(cleanQuery) || priorityNorm.contains(cleanQuery)) {
        results.add(SearchResultItem.TaskItem(task = task, isGoalTask = false))
      }
    }

    goals.forEach { goal ->
      goal.tasks.forEach { task ->
        val titleNorm = normalizeArabic(task.title)
        val notesNorm = normalizeArabic(task.notes ?: "")
        val goalTitleNorm = normalizeArabic(goal.title)

        if (titleNorm.contains(cleanQuery) || notesNorm.contains(cleanQuery) || goalTitleNorm.contains(cleanQuery)) {
          results.add(
            SearchResultItem.TaskItem(
              task = task,
              isGoalTask = true,
              goalTitle = goal.title,
              goalId = goal.id
            )
          )
        }
      }
    }
  }

  // 2. Search Habits
  if (category == SearchCategory.ALL || category == SearchCategory.HABITS) {
    habits.forEach { habit ->
      val titleNorm = normalizeArabic(habit.title)
      val unitNorm = normalizeArabic(habit.unit)
      val typeNorm = normalizeArabic(habit.type.name)

      if (titleNorm.contains(cleanQuery) || unitNorm.contains(cleanQuery) || typeNorm.contains(cleanQuery)) {
        results.add(SearchResultItem.HabitItem(habit))
      }
    }
  }

  // 3. Search Goals
  if (category == SearchCategory.ALL || category == SearchCategory.GOALS) {
    goals.forEach { goal ->
      val titleNorm = normalizeArabic(goal.title)
      val descNorm = normalizeArabic(goal.description ?: "")
      val catNorm = normalizeArabic(goal.category ?: "")

      if (titleNorm.contains(cleanQuery) || descNorm.contains(cleanQuery) || catNorm.contains(cleanQuery)) {
        results.add(SearchResultItem.GoalItem(goal))
      }
    }
  }

  // 4. Search Notes & Idea Inbox
  if (category == SearchCategory.ALL || category == SearchCategory.NOTES) {
    notes.forEach { note ->
      val titleNorm = normalizeArabic(note.title)
      val contentNorm = normalizeArabic(note.content)
      val tagNorm = normalizeArabic(note.tag)

      if (titleNorm.contains(cleanQuery) || contentNorm.contains(cleanQuery) || tagNorm.contains(cleanQuery)) {
        results.add(SearchResultItem.NoteItem(note))
      }
    }
  }

  // 5. Search Calendar & Daily History
  if (category == SearchCategory.ALL || category == SearchCategory.CALENDAR) {
    dailyHistory.forEach { (dateKey, history) ->
      val dateNorm = normalizeArabic(history.dateDisplayArabic)
      val keyNorm = normalizeArabic(dateKey)
      val dayOfWeekNorm = normalizeArabic(history.dayOfWeekArabic.name)

      if (dateNorm.contains(cleanQuery) || keyNorm.contains(cleanQuery) || dayOfWeekNorm.contains(cleanQuery)) {
        results.add(
          SearchResultItem.CalendarItem(
            dateKey = dateKey,
            dateDisplayArabic = history.dateDisplayArabic,
            summary = history
          )
        )
      }
    }
  }

  return results
}
