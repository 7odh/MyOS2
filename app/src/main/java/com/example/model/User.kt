package com.example.model

enum class ThemeMode(val titleArabic: String) {
  SYSTEM("تلقائي (حسب النظام)"),
  LIGHT("الوضع الفاتح ☀️"),
  DARK("الوضع الداكن 🌙")
}

data class User(
  val name: String = "أحمد",
  val motivationalSentence: String = "النجاح هو مجموع قرارات وانضباطات صغيرة.",
  val isMotivationEnabled: Boolean = true,
  val isRestModeActive: Boolean = false,
  val themeMode: ThemeMode = ThemeMode.SYSTEM
)
