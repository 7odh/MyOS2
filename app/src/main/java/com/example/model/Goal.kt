package com.example.model

enum class GoalStatus(val titleArabic: String) {
  ACTIVE("قيد التنفيذ"),
  PAUSED("متوقف مؤقتاً"),
  COMPLETED("مكتمل")
}

data class Goal(
  val id: String,
  val title: String,
  val description: String? = null,
  val category: String? = null,
  val iconId: String = "book",
  val iconEmoji: String = "📚",
  val priority: Priority = Priority.HIGH,
  val startDate: String? = "2025/09/20",
  val dueDate: String? = null,
  val isPaused: Boolean = false,
  val tasks: List<Task> = emptyList(),
  val createdAt: Long = System.currentTimeMillis(),
  val completedAt: Long? = null
) {
  val totalTasksCount: Int
    get() = tasks.size

  val completedTasksCount: Int
    get() = tasks.count { it.isCompleted }

  val progressPercentage: Int
    get() = if (tasks.isEmpty()) 0 else ((completedTasksCount.toFloat() / totalTasksCount.toFloat()) * 100).toInt()

  val status: GoalStatus
    get() = when {
      isPaused -> GoalStatus.PAUSED
      totalTasksCount > 0 && completedTasksCount == totalTasksCount -> GoalStatus.COMPLETED
      else -> GoalStatus.ACTIVE
    }

  // On the Home screen: Only tasks scheduled for TODAY appear underneath their goal,
  // ordered by priority: High -> Medium -> Low -> No Priority, with stable creation order.
  val todayTasks: List<Task>
    get() = tasks
      .filter { it.isDueToday }
      .sortedWith(
        compareBy<Task> { it.priority.rank }
          .thenBy { it.createdAt }
      )

  // In Goal Details: All tasks of this goal, sorted by Priority (High -> Medium -> Low -> None)
  val sortedTasks: List<Task>
    get() = tasks.sortedWith(
      compareBy<Task> { it.priority.rank }
        .thenBy { it.createdAt }
    )
}
