package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

data class BottomNavTab(
  val destination: ScreenDestination,
  val title: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun MyOSBottomNavigationBar(
  currentScreen: ScreenDestination,
  onTabSelected: (ScreenDestination) -> Unit,
  onMoreClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    BottomNavTab(ScreenDestination.HOME, "الرئيسية", Icons.Outlined.Home),
    BottomNavTab(ScreenDestination.GOALS, "الأهداف", Icons.Outlined.TrackChanges),
    BottomNavTab(ScreenDestination.HABITS, "العادات", Icons.Outlined.Spa),
    BottomNavTab(ScreenDestination.TASKS, "المهام", Icons.Outlined.CheckBox),
    BottomNavTab(ScreenDestination.CALENDAR, "التقويم", Icons.Outlined.CalendarMonth)
  )

  NavigationBar(
    containerColor = SurfaceWhite,
    tonalElevation = 8.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    tabs.forEach { tab ->
      val isSelected = currentScreen == tab.destination

      NavigationBarItem(
        selected = isSelected,
        onClick = {
          onTabSelected(tab.destination)
        },
        icon = {
          Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = BrightBlue,
          selectedTextColor = BrightBlue,
          indicatorColor = GoalBlueBg,
          unselectedIconColor = TextSecondary,
          unselectedTextColor = TextSecondary
        )
      )
    }
  }
}
