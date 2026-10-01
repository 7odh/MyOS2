package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.model.ThemeMode
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.extraColors

@Composable
fun NavigationDrawerContent(
  currentScreen: ScreenDestination,
  onScreenSelected: (ScreenDestination) -> Unit,
  onCloseDrawer: () -> Unit,
  themeMode: ThemeMode = ThemeMode.SYSTEM,
  onThemeModeChanged: (ThemeMode) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors

  Column(
    modifier = modifier
      .fillMaxHeight()
      .width(290.dp)
      .background(extra.cardSurface)
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
            color = extra.textPrimary
          )
          Text(
            text = "عقلك الثاني",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            ),
            color = BrandPrimary
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
          tint = extra.textSecondary,
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

      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(color = extra.border, thickness = 1.dp)
      Spacer(modifier = Modifier.height(14.dp))

      // Theme Mode Toggle (فاتح / داكن / تلقائي)
      Text(
        text = "مظهر التطبيق",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.5.sp
        ),
        color = extra.textSecondary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(extra.secondarySurface)
          .border(1.dp, extra.border, RoundedCornerShape(12.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        ThemeToggleChip(
          title = "فاتح",
          icon = Icons.Outlined.LightMode,
          isSelected = themeMode == ThemeMode.LIGHT,
          onClick = { onThemeModeChanged(ThemeMode.LIGHT) },
          modifier = Modifier.weight(1f)
        )
        ThemeToggleChip(
          title = "داكن",
          icon = Icons.Outlined.DarkMode,
          isSelected = themeMode == ThemeMode.DARK,
          onClick = { onThemeModeChanged(ThemeMode.DARK) },
          modifier = Modifier.weight(1f)
        )
        ThemeToggleChip(
          title = "تلقائي",
          icon = Icons.Outlined.SettingsBrightness,
          isSelected = themeMode == ThemeMode.SYSTEM,
          onClick = { onThemeModeChanged(ThemeMode.SYSTEM) },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun ThemeToggleChip(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) BrandPrimary else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (isSelected) Color.White else extra.textSecondary,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
          fontSize = 11.sp
        ),
        color = if (isSelected) Color.White else extra.textSecondary
      )
    }
  }
}

@Composable
private fun DrawerMenuItem(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val backgroundColor = if (isSelected) extra.secondarySurface else Color.Transparent
  val contentColor = if (isSelected) BrandPrimary else extra.textPrimary

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = contentColor,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
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
