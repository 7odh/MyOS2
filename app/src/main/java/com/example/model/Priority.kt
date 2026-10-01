package com.example.model

enum class Priority(val titleArabic: String, val rank: Int) {
  HIGH("عالية", 1),
  MEDIUM("متوسطة", 2),
  LOW("منخفضة", 3),
  NONE("بدون أولوية", 4)
}
