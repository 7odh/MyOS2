package com.example.model

data class User(
  val name: String = "أحمد",
  val motivationalSentence: String = "كل يوم هو فرصة جديدة لتكون أفضل من أمس",
  val isMotivationEnabled: Boolean = true,
  val isRestModeActive: Boolean = false
)
