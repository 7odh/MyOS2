package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TaskSchedule(val titleArabic: String) {
  TODAY("اليوم"),
  TOMORROW("غداً"),
  FUTURE("قريباً"),
  NO_DATE("بدون موعد")
}

fun getTodayDateString(): String {
  val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
  return sdf.format(Date())
}

fun getTomorrowDateString(): String {
  val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
  val cal = java.util.Calendar.getInstance()
  cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
  return sdf.format(cal.time)
}

data class Task(
  val id: String,
  val title: String,
  val notes: String? = null,
  val priority: Priority = Priority.MEDIUM,
  val isCompleted: Boolean = false,
  val goalId: String? = null,
  val isGoalTask: Boolean = false,
  val category: String? = null,
  val schedule: TaskSchedule = TaskSchedule.TODAY,
  val dueDateFormatted: String? = null,
  val isPostponed: Boolean = false,
  val postponedCount: Int = 0,
  val postponedFromDate: String? = null,
  val createdAt: Long = System.currentTimeMillis(),
  val completedAt: Long? = null
) {
  // Only tasks that are due today appear in the Home screen
  val isDueToday: Boolean
    get() {
      // Tasks without a date never appear in Home
      if (schedule == TaskSchedule.NO_DATE) return false
      // Tasks explicitly marked for TODAY always appear in Home
      if (schedule == TaskSchedule.TODAY) return true
      // Tasks explicitly marked for TOMORROW do not appear in Home
      if (schedule == TaskSchedule.TOMORROW) return false
      // For tasks with a formatted due date, check if date matches today
      if (!dueDateFormatted.isNullOrBlank()) {
        val clean = dueDateFormatted.trim().replace('-', '/')
        val today1 = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(Date())
        val today2 = SimpleDateFormat("yyyy/M/d", Locale.ENGLISH).format(Date())
        return clean == today1 || clean == today2
      }
      return false
    }
}
