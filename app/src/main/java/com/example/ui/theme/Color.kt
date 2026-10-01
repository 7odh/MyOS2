package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// MyOS Design System Palette (Light & Dark)
// ==========================================

// --- Light Mode Tokens (ألوان التصميم فاتح) ---
val LightBaseBackground = Color(0xFFF8FAFF)
val LightCardSurface = Color(0xFFFFFFFF)
val LightSecondarySurface = Color(0xFFF1F5FF)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)
val LightBorder = Color(0xFFE2E8F0)
val LightBorderSubtle = Color(0xFFE0E7FF)
val LightShadow = Color(0xFFC7D2FE)

// --- Dark Mode Tokens (ألوان التصميم داكن) ---
val DarkBaseBackground = Color(0xFF0B1220)
val DarkCardSurface = Color(0xFF111827)
val DarkSecondarySurface = Color(0xFF1E293B)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkBorder = Color(0xFF334155)
val DarkBorderSubtle = Color(0xFF1E293B)
val DarkShadow = Color(0xFF000000)

// --- Shared Core Brand & Category Colors ---
val BrandPrimary = Color(0xFF3B82F6) // اللون الأساسي #3B82F6
val BrightBlue = Color(0xFF3B82F6)
val DeepBlue = Color(0xFF2563EB)
val IndigoPrimary = Color(0xFF6366F1)
val ElectricCyan = Color(0xFF22D3EE)
val AmberAccent = Color(0xFFF59E0B)

// النجاح / العادات (Habits & Success)
val HabitEmerald = Color(0xFF10B981) // #10B981
val HabitEmeraldBgLight = Color(0xFFECFDF5)
val HabitEmeraldBgDark = Color(0xFF064E3B)
val HabitEmeraldTrackLight = Color(0xFFD1FAE5)
val HabitEmeraldTrackDark = Color(0xFF065F46)

// التحذيرات / المهام (Tasks & Warnings)
val TaskViolet = Color(0xFF8B5CF6) // #8B5CF6
val TaskVioletBgLight = Color(0xFFF5F3FF)
val TaskVioletBgDark = Color(0xFF3B0764)
val TaskVioletTrackLight = Color(0xFFEDE9FE)
val TaskVioletTrackDark = Color(0xFF4C1D95)

// الأهداف (Goals)
val GoalBlue = Color(0xFF3B82F6)
val GoalBlueBgLight = Color(0xFFEFF6FF)
val GoalBlueBgDark = Color(0xFF1E3A8A)
val GoalBlueTrackLight = Color(0xFFDBEAFE)
val GoalBlueTrackDark = Color(0xFF1E40AF)

// وضع الراحة (Rest Mode)
val RestLavenderBgLight = Color(0xFFEEF2FF)
val RestLavenderBgDark = Color(0xFF1E1B4B)
val RestLavenderActive = Color(0xFF6366F1)
val RestLavenderText = Color(0xFF6366F1)

// درجات الأولوية (Priority)
val PriorityHigh = Color(0xFFEF4444)
val PriorityHighBg = Color(0xFFFEF2F2)
val PriorityMedium = Color(0xFF3B82F6)
val PriorityMediumBg = Color(0xFFEFF6FF)
val PriorityLow = Color(0xFF64748B)
val PriorityLowBg = Color(0xFFF1F5F9)

// تحليلات وأركان الإنتاجية
val AnalyticsProgressEmerald = Color(0xFF10B981)
val AnalyticsProgressEmeraldBg = Color(0xFFECFDF5)
val AnalyticsShortfallRed = Color(0xFFEF4444)
val AnalyticsShortfallRedBg = Color(0xFFFEF2F2)
val AnalyticsRestAmber = Color(0xFFF59E0B)
val AnalyticsRestAmberBg = Color(0xFFFFFBEB)
val AnalyticsPostponeSky = Color(0xFF0284C7)
val AnalyticsPostponeSkyBg = Color(0xFFE0F2FE)
val AnalyticsFocusViolet = Color(0xFF8B5CF6)
val AnalyticsFocusVioletBg = Color(0xFFF5F3FF)

// Legacy alias mappings for backwards compatibility across existing components
val BackgroundLight = LightBaseBackground
val SurfaceWhite = LightCardSurface
val SurfaceCard = LightCardSurface
val BorderLight = LightBorder
val BorderVeryLight = LightBorderSubtle
val TextPrimary = LightTextPrimary
val TextSecondary = LightTextSecondary
val TextMuted = LightTextSecondary
val TextWhite = Color(0xFFFFFFFF)
val ElectricViolet = TaskViolet

val HabitEmeraldBg = HabitEmeraldBgLight
val HabitEmeraldTrack = HabitEmeraldTrackLight
val TaskVioletBg = TaskVioletBgLight
val TaskVioletTrack = TaskVioletTrackLight
val GoalBlueBg = GoalBlueBgLight
val GoalBlueTrack = GoalBlueTrackLight
val RestLavenderBg = RestLavenderBgLight
