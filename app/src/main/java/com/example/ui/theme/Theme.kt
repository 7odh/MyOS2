package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MyOSLightColorScheme = lightColorScheme(
  primary = IndigoPrimary,
  onPrimary = TextWhite,
  primaryContainer = GoalBlueBg,
  onPrimaryContainer = DeepBlue,
  secondary = BrightBlue,
  onSecondary = TextWhite,
  secondaryContainer = RestLavenderBg,
  onSecondaryContainer = RestLavenderText,
  tertiary = ElectricCyan,
  onTertiary = TextPrimary,
  background = BackgroundLight,
  onBackground = TextPrimary,
  surface = SurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = BorderVeryLight,
  onSurfaceVariant = TextSecondary,
  outline = BorderLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Specification states: "التصميم يدعم الوضع الفاتح فقط (في هذه المرحلة)" - Light mode primary
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = MyOSLightColorScheme,
    typography = Typography,
    content = content
  )
}
