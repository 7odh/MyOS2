package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.extraColors

data class BottomNavTabItem(
  val destination: ScreenDestination,
  val title: String,
  val icon: ImageVector
)

@Composable
fun MyOSBottomNavigationBar(
  currentScreen: ScreenDestination,
  onTabSelected: (ScreenDestination) -> Unit,
  onAddClick: (() -> Unit)? = null,
  onMoreClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val isDark = extra.isDark
  val centerAction = onAddClick ?: onMoreClick ?: {}

  val rightTabs = listOf(
    BottomNavTabItem(ScreenDestination.HOME, "الرئيسية", Icons.Outlined.Home),
    BottomNavTabItem(ScreenDestination.GOALS, "الأهداف", Icons.Outlined.Whatshot)
  )

  val leftTabs = listOf(
    BottomNavTabItem(ScreenDestination.HABITS, "العادات", Icons.Outlined.Spa),
    BottomNavTabItem(ScreenDestination.TASKS, "المهام", Icons.Outlined.CheckBox)
  )

  // Outer container with height 80.dp + navigationBarsPadding.
  // This ensures the elevated FAB at the top is completely within layout bounds and NEVER clipped!
  Box(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .height(82.dp),
    contentAlignment = Alignment.BottomCenter
  ) {
    // 1. Navigation Surface Bar docked to the bottom (height 64.dp)
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .border(1.dp, extra.border),
      color = extra.cardSurface,
      shadowElevation = if (isDark) 0.dp else 6.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Right tabs: الرئيسية (Home), الأهداف (Goals)
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          rightTabs.forEach { tab ->
            NavTabItem(
              tab = tab,
              isSelected = currentScreen == tab.destination,
              onClick = { onTabSelected(tab.destination) }
            )
          }
        }

        // Center space reserved for the FAB
        Spacer(modifier = Modifier.width(68.dp))

        // Left tabs: العادات (Habits), المهام (Tasks)
        Row(
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          leftTabs.forEach { tab ->
            NavTabItem(
              tab = tab,
              isSelected = currentScreen == tab.destination,
              onClick = { onTabSelected(tab.destination) }
            )
          }
        }
      }
    }

    // 2. Full Round Elevated Floating Action Button (FAB)
    // Sits at TopCenter with 56.dp size, completely unclipped!
    Box(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .size(56.dp)
        .shadow(
          elevation = 8.dp,
          shape = CircleShape,
          ambientColor = if (isDark) Color.Black else extra.shadow,
          spotColor = if (isDark) Color.Black else BrandPrimary
        )
        .clip(CircleShape)
        .background(BrandPrimary)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = centerAction
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "إضافة سريعة",
        tint = Color.White,
        modifier = Modifier.size(28.dp)
      )
    }
  }
}

@Composable
private fun NavTabItem(
  tab: BottomNavTabItem,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val extra = MaterialTheme.extraColors
  val tintColor = if (isSelected) BrandPrimary else extra.textSecondary

  Column(
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 10.dp, vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = tab.icon,
      contentDescription = tab.title,
      tint = tintColor,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = tab.title,
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
      ),
      color = tintColor
    )
  }
}
