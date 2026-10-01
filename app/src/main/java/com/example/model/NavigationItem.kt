package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenDestination(
  val route: String,
  val titleArabic: String,
  val icon: ImageVector,
  val isBottomNavTab: Boolean = false
) {
  HOME("home", "الرئيسية", Icons.Outlined.Home, true),
  GOALS("goals", "الأهداف", Icons.Outlined.TrackChanges, true),
  HABITS("habits", "العادات", Icons.Outlined.Spa, true),
  TASKS("tasks", "المهام", Icons.Outlined.CheckBox, true),
  CALENDAR("calendar", "التقويم", Icons.Outlined.CalendarMonth, true),
  PROJECTS("projects", "المشاريع", Icons.Outlined.FolderSpecial),
  LISTS("lists", "القوائم", Icons.AutoMirrored.Outlined.List),
  NOTES("notes", "الملاحظات", Icons.Outlined.Description),
  KNOWLEDGE("knowledge", "المعرفة", Icons.Outlined.MenuBook),
  FOCUS("focus", "التركيز", Icons.Outlined.CenterFocusStrong),
  ANALYTICS("analytics", "التحليلات", Icons.Outlined.BarChart),
  SEARCH("search", "البحث", Icons.Outlined.Search),
  MORE("more", "المزيد", Icons.AutoMirrored.Outlined.List, false),
  SETTINGS("settings", "الإعدادات", Icons.Outlined.Settings),
  HELP("help", "مساعدة", Icons.Outlined.HelpOutline)
}
