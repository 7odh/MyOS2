package com.example.model

data class DailyAnalytics(
  val habitsCompleted: Int,
  val habitsTotal: Int,
  val tasksCompleted: Int,
  val tasksTotal: Int,
  val goalsCompleted: Int,
  val goalsTotal: Int
) {
  val habitsPercentage: Int
    get() = if (habitsTotal > 0) ((habitsCompleted.toFloat() / habitsTotal) * 100).toInt() else 0

  val habitsRemaining: Int
    get() = (habitsTotal - habitsCompleted).coerceAtLeast(0)

  val tasksPercentage: Int
    get() = if (tasksTotal > 0) ((tasksCompleted.toFloat() / tasksTotal) * 100).toInt() else 0

  val tasksRemaining: Int
    get() = (tasksTotal - tasksCompleted).coerceAtLeast(0)

  val goalsPercentage: Int
    get() = if (goalsTotal > 0) ((goalsCompleted.toFloat() / goalsTotal) * 100).toInt() else 0

  val goalsRemaining: Int
    get() = (goalsTotal - goalsCompleted).coerceAtLeast(0)
}
