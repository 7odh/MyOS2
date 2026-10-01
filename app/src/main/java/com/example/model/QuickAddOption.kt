package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.IndigoPrimary

enum class QuickAddType(
  val titleArabic: String,
  val icon: ImageVector,
  val accentColor: Color
) {
  TASK("إضافة مهمة", Icons.Outlined.CheckBox, BrightBlue),
  HABIT("إضافة عادة", Icons.Outlined.Spa, HabitEmerald),
  GOAL("إضافة هدف", Icons.Outlined.TrackChanges, IndigoPrimary),
  NOTE("إضافة ملاحظة", Icons.Outlined.Description, Color(0xFFF59E0B)),
  PROJECT("إضافة مشروع", Icons.Outlined.FolderSpecial, ElectricViolet),
  LIST("إضافة قائمة", Icons.AutoMirrored.Outlined.List, ElectricCyan),
  EVENT("إضافة حدث", Icons.Outlined.CalendarMonth, DeepBlue),
  FILE("إضافة ملف", Icons.AutoMirrored.Outlined.InsertDriveFile, Color(0xFF64748B))
}
