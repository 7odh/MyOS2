package com.example.model

enum class DayOfWeekArabic(val id: Int, val shortArabic: String, val fullArabic: String) {
  SATURDAY(1, "سبت", "السبت"),
  SUNDAY(2, "أحد", "الأحد"),
  MONDAY(3, "اثنين", "الإثنين"),
  TUESDAY(4, "ثلاثاء", "الثلاثاء"),
  WEDNESDAY(5, "أربعاء", "الأربعاء"),
  THURSDAY(6, "خميس", "الخميس"),
  FRIDAY(7, "جمعة", "الجمعة")
}

enum class HabitFrequency(val titleArabic: String) {
  DAILY("يومياً"),
  SPECIFIC_DAYS("أيام محددة في الأسبوع")
}

enum class HabitType(val titleArabic: String) {
  BOOLEAN("تأكيد لمرة واحدة"),    // Single checkmark per day
  QUANTITY("كمية / عدد"),         // Pages, pushups, jumps, etc.
  DURATION("وقت / مدة"),          // Minutes of practice, gym, etc.
  COUNTER("تكرار / ضغطات")        // 5 prayers, 8 cups of water
}

data class Habit(
  val id: String,
  val title: String,
  val type: HabitType = HabitType.BOOLEAN,
  val targetValue: Int = 1,
  val currentValue: Int = 0,
  val unit: String = "مرة", // "صفحات", "عدات", "نطات", "دقيقة", "صلوات", "أكواب", إلخ
  val frequency: HabitFrequency = HabitFrequency.DAILY,
  val scheduledDays: List<DayOfWeekArabic> = emptyList(), // e.g. [SATURDAY, MONDAY, THURSDAY]
  val isMandatory: Boolean = false, // If true: never hidden by Rest Mode (e.g. prayer)
  val priority: Priority = Priority.HIGH,
  val currentStreak: Int = 12,
  val iconEmoji: String = "🌱",
  val isPaused: Boolean = false,
  val pauseUntilDate: Long? = null, // epoch millis until which habit is paused, or null if indefinite/not paused
  val pausedAt: Long? = null
) {
  val isCompleted: Boolean
    get() = currentValue >= targetValue

  val progressPercentage: Int
    get() = if (targetValue <= 0) {
      if (isCompleted) 100 else 0
    } else {
      ((currentValue.toFloat() / targetValue.toFloat()) * 100).toInt()
    }

  val isCurrentlyPaused: Boolean
    get() {
      if (!isPaused) return false
      val until = pauseUntilDate ?: return true
      return System.currentTimeMillis() < until
    }

  val pauseRemainingDays: Int?
    get() {
      if (!isCurrentlyPaused) return null
      val until = pauseUntilDate ?: return null
      val diffMs = until - System.currentTimeMillis()
      if (diffMs <= 0) return 0
      return Math.ceil(diffMs.toDouble() / (1000.0 * 60 * 60 * 24)).toInt()
    }

  val pauseStatusDescription: String?
    get() {
      if (!isCurrentlyPaused) return null
      val days = pauseRemainingDays
      return if (days == null) {
        "مركونة مؤقتاً حتى الاستئناف يدوياً ⏸️"
      } else {
        val daysWord = when {
          days == 1 -> "يوم واحد"
          days == 2 -> "يومان"
          days in 3..10 -> "$days أيام"
          else -> "$days يوماً"
        }
        "مركونة مؤقتاً (متبقي $daysWord) ⏸️"
      }
    }

  fun isScheduledForToday(todayDayOfWeek: DayOfWeekArabic): Boolean {
    // If the habit is paused / frozen, it does NOT appear on the Home dashboard
    if (isCurrentlyPaused) return false
    return when (frequency) {
      HabitFrequency.DAILY -> true
      HabitFrequency.SPECIFIC_DAYS -> scheduledDays.isNotEmpty() && scheduledDays.contains(todayDayOfWeek)
    }
  }

  val scheduleDescription: String
    get() = when (frequency) {
      HabitFrequency.DAILY -> "يومياً"
      HabitFrequency.SPECIFIC_DAYS -> {
        if (scheduledDays.isEmpty()) "غير محدد"
        else scheduledDays.joinToString("، ") { it.shortArabic }
      }
    }
}

fun getCurrentDayOfWeekArabic(): DayOfWeekArabic {
  val cal = java.util.Calendar.getInstance()
  return when (cal.get(java.util.Calendar.DAY_OF_WEEK)) {
    java.util.Calendar.SATURDAY -> DayOfWeekArabic.SATURDAY
    java.util.Calendar.SUNDAY -> DayOfWeekArabic.SUNDAY
    java.util.Calendar.MONDAY -> DayOfWeekArabic.MONDAY
    java.util.Calendar.TUESDAY -> DayOfWeekArabic.TUESDAY
    java.util.Calendar.WEDNESDAY -> DayOfWeekArabic.WEDNESDAY
    java.util.Calendar.THURSDAY -> DayOfWeekArabic.THURSDAY
    java.util.Calendar.FRIDAY -> DayOfWeekArabic.FRIDAY
    else -> DayOfWeekArabic.SATURDAY
  }
}
