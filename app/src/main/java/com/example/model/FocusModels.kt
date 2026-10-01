package com.example.model

enum class FocusTimerMode(val titleArabic: String, val defaultMinutes: Int, val emoji: String) {
  POMODORO("بومودورو", 25, "🍅"),
  SHORT_BREAK("استراحة قصيرة", 5, "☕"),
  LONG_BREAK("استراحة طويلة", 15, "🌴"),
  CUSTOM("مخصص", 30, "⏱️")
}

enum class FocusAttachType(val titleArabic: String, val emoji: String) {
  NONE("جلسة تركيز حرة", "🎯"),
  HABIT("عادة يومية", "🌱"),
  GENERAL_TASK("مهمة عامة", "⚡"),
  GOAL_TASK("مهمة هدف", "🎯")
}

data class FocusAttachment(
  val type: FocusAttachType = FocusAttachType.NONE,
  val id: String = "",
  val goalId: String? = null,
  val title: String = "جلسة تركيز حرة",
  val iconEmoji: String = "🎯",
  val targetMinutes: Int? = null,
  val currentMinutes: Int? = null,
  val unit: String? = null,
  val isTimedHabit: Boolean = false
)

enum class AmbientSoundType {
  NONE,
  RAIN,
  OCEAN,
  FIRE,
  SNOW,
  FOREST
}

data class AmbientSound(
  val id: String,
  val nameArabic: String,
  val emoji: String,
  val type: AmbientSoundType
)

object AmbientSoundProvider {
  val defaultSounds = listOf(
    AmbientSound("rain", "أصوات المطر", "🌧️", AmbientSoundType.RAIN),
    AmbientSound("ocean", "أمواج البحر", "🌊", AmbientSoundType.OCEAN),
    AmbientSound("fire", "موقد النار", "🔥", AmbientSoundType.FIRE),
    AmbientSound("snow", "رياح الثلج الهادئة", "❄️", AmbientSoundType.SNOW),
    AmbientSound("forest", "هدوء الغابة", "🌲", AmbientSoundType.FOREST)
  )
}
