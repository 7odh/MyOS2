package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light Mode Color Scheme matching mockup
val MyOSLightColorScheme = lightColorScheme(
  primary = BrandPrimary, // #3B82F6
  onPrimary = Color.White,
  primaryContainer = LightSecondarySurface, // #F1F5FF
  onPrimaryContainer = BrandPrimary,
  secondary = BrightBlue,
  onSecondary = Color.White,
  secondaryContainer = LightSecondarySurface,
  onSecondaryContainer = BrandPrimary,
  tertiary = HabitEmerald,
  onTertiary = Color.White,
  background = LightBaseBackground, // #F8FAFF
  onBackground = LightTextPrimary, // #0F172A
  surface = LightCardSurface, // #FFFFFF
  onSurface = LightTextPrimary, // #0F172A
  surfaceVariant = LightSecondarySurface, // #F1F5FF
  onSurfaceVariant = LightTextSecondary, // #64748B
  outline = LightBorder, // #E2E8F0
  outlineVariant = LightBorderSubtle
)

// Dark Mode Color Scheme matching mockup
val MyOSDarkColorScheme = darkColorScheme(
  primary = BrandPrimary, // #3B82F6
  onPrimary = Color.White,
  primaryContainer = DarkSecondarySurface, // #1E293B
  onPrimaryContainer = Color.White,
  secondary = BrightBlue,
  onSecondary = Color.White,
  secondaryContainer = DarkSecondarySurface,
  onSecondaryContainer = DarkTextSecondary,
  tertiary = HabitEmerald,
  onTertiary = Color.White,
  background = DarkBaseBackground, // #0B1220
  onBackground = DarkTextPrimary, // #FFFFFF
  surface = DarkCardSurface, // #111827
  onSurface = DarkTextPrimary, // #FFFFFF
  surfaceVariant = DarkSecondarySurface, // #1E293B
  onSurfaceVariant = DarkTextSecondary, // #94A3B8
  outline = DarkBorder, // #334155
  outlineVariant = DarkBorderSubtle
)

data class MyOSExtraColors(
  val cardSurface: Color,
  val secondarySurface: Color,
  val border: Color,
  val borderSubtle: Color,
  val shadow: Color,
  val textPrimary: Color,
  val textSecondary: Color,
  val habitBg: Color,
  val habitTrack: Color,
  val taskBg: Color,
  val taskTrack: Color,
  val goalBg: Color,
  val goalTrack: Color,
  val isDark: Boolean
)

val LocalMyOSExtraColors = staticCompositionLocalOf {
  MyOSExtraColors(
    cardSurface = LightCardSurface,
    secondarySurface = LightSecondarySurface,
    border = LightBorder,
    borderSubtle = LightBorderSubtle,
    shadow = LightShadow,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    habitBg = HabitEmeraldBgLight,
    habitTrack = HabitEmeraldTrackLight,
    taskBg = TaskVioletBgLight,
    taskTrack = TaskVioletTrackLight,
    goalBg = GoalBlueBgLight,
    goalTrack = GoalBlueTrackLight,
    isDark = false
  )
}

val MaterialTheme.extraColors: MyOSExtraColors
  @Composable
  @ReadOnlyComposable
  get() = LocalMyOSExtraColors.current

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) MyOSDarkColorScheme else MyOSLightColorScheme
  val extraColors = if (darkTheme) {
    MyOSExtraColors(
      cardSurface = DarkCardSurface,
      secondarySurface = DarkSecondarySurface,
      border = DarkBorder,
      borderSubtle = DarkBorderSubtle,
      shadow = DarkShadow,
      textPrimary = DarkTextPrimary,
      textSecondary = DarkTextSecondary,
      habitBg = DarkSecondarySurface,
      habitTrack = HabitEmeraldTrackDark,
      taskBg = DarkSecondarySurface,
      taskTrack = TaskVioletTrackDark,
      goalBg = DarkSecondarySurface,
      goalTrack = GoalBlueTrackDark,
      isDark = true
    )
  } else {
    MyOSExtraColors(
      cardSurface = LightCardSurface,
      secondarySurface = LightSecondarySurface,
      border = LightBorder,
      borderSubtle = LightBorderSubtle,
      shadow = LightShadow,
      textPrimary = LightTextPrimary,
      textSecondary = LightTextSecondary,
      habitBg = HabitEmeraldBgLight,
      habitTrack = HabitEmeraldTrackLight,
      taskBg = TaskVioletBgLight,
      taskTrack = TaskVioletTrackLight,
      goalBg = GoalBlueBgLight,
      goalTrack = GoalBlueTrackLight,
      isDark = false
    )
  }

  CompositionLocalProvider(LocalMyOSExtraColors provides extraColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}
