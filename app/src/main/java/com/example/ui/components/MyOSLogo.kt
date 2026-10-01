package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricViolet

@Composable
fun MyOSLogoIcon(
  modifier: Modifier = Modifier,
  size: Dp = 36.dp
) {
  Box(modifier = modifier.size(size)) {
    Canvas(modifier = Modifier.size(size)) {
      val w = this.size.width
      val h = this.size.height

      // Gradient for the stylized "M" logo
      val gradientBrush = Brush.linearGradient(
        colors = listOf(
          ElectricCyan,
          BrightBlue,
          ElectricViolet
        ),
        start = Offset(0f, h * 0.5f),
        end = Offset(w, h * 0.5f)
      )

      val strokeWidth = w * 0.20f

      // Left arch of M
      val leftArch = Path().apply {
        moveTo(w * 0.12f, h * 0.85f)
        cubicTo(
          w * 0.12f, h * 0.20f,
          w * 0.46f, h * 0.20f,
          w * 0.50f, h * 0.70f
        )
      }

      // Right arch of M
      val rightArch = Path().apply {
        moveTo(w * 0.50f, h * 0.70f)
        cubicTo(
          w * 0.54f, h * 0.20f,
          w * 0.88f, h * 0.20f,
          w * 0.88f, h * 0.85f
        )
      }

      drawPath(
        path = leftArch,
        brush = gradientBrush,
        style = Stroke(
          width = strokeWidth,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )

      drawPath(
        path = rightArch,
        brush = gradientBrush,
        style = Stroke(
          width = strokeWidth,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )
    }
  }
}
