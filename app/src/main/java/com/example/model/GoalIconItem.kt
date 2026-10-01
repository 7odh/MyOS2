package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class GoalIconOption(
  val id: String,
  val nameArabic: String,
  val emoji: String,
  val icon: ImageVector,
  val tintColor: Color,
  val bgColor: Color
)

object GoalIconProvider {
  val defaultIcons: List<GoalIconOption> = listOf(
    GoalIconOption(
      id = "book",
      nameArabic = "تعليم",
      emoji = "📚",
      icon = Icons.Outlined.AutoStories,
      tintColor = Color(0xFF2563EB),
      bgColor = Color(0xFFEFF6FF)
    ),
    GoalIconOption(
      id = "code",
      nameArabic = "برمجة",
      emoji = "</>",
      icon = Icons.Outlined.Code,
      tintColor = Color(0xFF4F46E5),
      bgColor = Color(0xFFEEF2FF)
    ),
    GoalIconOption(
      id = "fitness",
      nameArabic = "لياقة",
      emoji = "🏋️",
      icon = Icons.Outlined.FitnessCenter,
      tintColor = Color(0xFF0D9488),
      bgColor = Color(0xFFF0FDFA)
    ),
    GoalIconOption(
      id = "tech",
      nameArabic = "تقنية",
      emoji = "💻",
      icon = Icons.Outlined.Laptop,
      tintColor = Color(0xFF0284C7),
      bgColor = Color(0xFFF0F9FF)
    ),
    GoalIconOption(
      id = "health",
      nameArabic = "صحة",
      emoji = "❤️",
      icon = Icons.Outlined.Favorite,
      tintColor = Color(0xFFE11D48),
      bgColor = Color(0xFFFFF1F2)
    ),
    GoalIconOption(
      id = "travel",
      nameArabic = "سفر",
      emoji = "✈️",
      icon = Icons.Outlined.Flight,
      tintColor = Color(0xFF7C3AED),
      bgColor = Color(0xFFF5F3FF)
    ),
    GoalIconOption(
      id = "camera",
      nameArabic = "تصوير",
      emoji = "📷",
      icon = Icons.Outlined.PhotoCamera,
      tintColor = Color(0xFF9333EA),
      bgColor = Color(0xFFFAF5FF)
    ),
    GoalIconOption(
      id = "art",
      nameArabic = "فنون",
      emoji = "🎨",
      icon = Icons.Outlined.Palette,
      tintColor = Color(0xFFEA580C),
      bgColor = Color(0xFFFFF7ED)
    ),
    GoalIconOption(
      id = "finance",
      nameArabic = "مالية",
      emoji = "💰",
      icon = Icons.Outlined.AccountBalanceWallet,
      tintColor = Color(0xFFD97706),
      bgColor = Color(0xFFFFFBEB)
    ),
    GoalIconOption(
      id = "target",
      nameArabic = "أهداف",
      emoji = "🎯",
      icon = Icons.Outlined.TrackChanges,
      tintColor = Color(0xFF0891B2),
      bgColor = Color(0xFFECFEFF)
    ),
    GoalIconOption(
      id = "ideas",
      nameArabic = "أفكار",
      emoji = "💡",
      icon = Icons.Outlined.Lightbulb,
      tintColor = Color(0xFFCA8A04),
      bgColor = Color(0xFFFEFCE8)
    )
  )

  fun getIcon(id: String?): GoalIconOption {
    return defaultIcons.find { it.id == id } ?: defaultIcons.first()
  }
}
