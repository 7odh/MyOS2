package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NavigationDrawerContent(
  currentScreen: ScreenDestination,
  onScreenSelected: (ScreenDestination) -> Unit,
  onCloseDrawer: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxHeight()
      .width(280.dp)
      .background(SurfaceWhite)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Drawer Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        MyOSLogoIcon(size = 32.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "MyOS",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "عقلك الثاني",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 10.sp
            ),
            color = TextSecondary
          )
        }
      }

      IconButton(
        onClick = onCloseDrawer,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "إغلاق القائمة",
          tint = TextSecondary,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Scrollable navigation list
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
    ) {
      // Main destinations (Cleaned: removed inactive Projects and Knowledge; Search is now in top bar)
      val mainItems = listOf(
        ScreenDestination.HOME,
        ScreenDestination.GOALS,
        ScreenDestination.HABITS,
        ScreenDestination.TASKS,
        ScreenDestination.CALENDAR,
        ScreenDestination.LISTS,
        ScreenDestination.NOTES,
        ScreenDestination.FOCUS,
        ScreenDestination.ANALYTICS
      )

      mainItems.forEach { destination ->
        DrawerMenuItem(
          title = destination.titleArabic,
          icon = destination.icon,
          isSelected = currentScreen == destination,
          onClick = {
            onScreenSelected(destination)
            onCloseDrawer()
          }
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = BorderLight.copy(alpha = 0.6f), thickness = 1.dp)
      Spacer(modifier = Modifier.height(10.dp))

      // Bottom utility destinations
      val bottomItems = listOf(
        ScreenDestination.SETTINGS,
        ScreenDestination.HELP
      )

      bottomItems.forEach { destination ->
        DrawerMenuItem(
          title = destination.titleArabic,
          icon = destination.icon,
          isSelected = currentScreen == destination,
          onClick = {
            onScreenSelected(destination)
            onCloseDrawer()
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom decorative motivational mountain card
      BottomMountainCard()
    }
  }
}

@Composable
private fun DrawerMenuItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgColor = if (isSelected) GoalBlueBg else Color.Transparent
  val contentColor = if (isSelected) BrightBlue else TextPrimary

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = contentColor,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        fontSize = 14.sp
      ),
      color = contentColor
    )
  }
}

@Composable
private fun BottomMountainCard(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(100.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFEFF6FF)
          )
        )
      )
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // Soft mountain silhouette
      val path = Path().apply {
        moveTo(w * 0.1f, h)
        lineTo(w * 0.35f, h * 0.45f)
        lineTo(w * 0.65f, h)
        close()
      }
      drawPath(
        path = path,
        color = Color(0xFFDBEAFE).copy(alpha = 0.6f)
      )

      val summitPath = Path().apply {
        moveTo(w * 0.25f, h)
        lineTo(w * 0.42f, h * 0.32f)
        lineTo(w * 0.6f, h)
        close()
      }
      drawPath(
        path = summitPath,
        color = Color(0xFF93C5FD).copy(alpha = 0.5f)
      )

      // Flag on summit
      drawLine(
        color = Color(0xFF1E293B),
        start = Offset(w * 0.42f, h * 0.32f),
        end = Offset(w * 0.42f, h * 0.20f),
        strokeWidth = 1.5.dp.toPx()
      )
      val flag = Path().apply {
        moveTo(w * 0.42f, h * 0.20f)
        lineTo(w * 0.48f, h * 0.24f)
        lineTo(w * 0.42f, h * 0.28f)
        close()
      }
      drawPath(path = flag, color = Color(0xFFEF4444))
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "\"رحلتك للأفضل\nتبدأ من هنا\"",
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          lineHeight = 15.sp
        ),
        color = TextSecondary
      )
      Text(
        text = "MyOS",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        ),
        color = TextMuted
      )
    }
  }
}
