package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FreeBreakfast
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.RestLavenderActive
import com.example.ui.theme.extraColors

@Composable
fun MyOSHeader(
  isRestModeActive: Boolean,
  onMenuClick: () -> Unit,
  onRestModeToggle: () -> Unit,
  onSearchClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val isDark = extra.isDark

  val restBgColor by animateColorAsState(
    targetValue = if (isRestModeActive) {
      RestLavenderActive
    } else {
      extra.secondarySurface
    },
    animationSpec = tween(durationMillis = 300),
    label = "restBgColor"
  )

  val restTextColor by animateColorAsState(
    targetValue = if (isRestModeActive) {
      Color.White
    } else {
      if (isDark) Color(0xFFA5B4FC) else Color(0xFF6366F1)
    },
    animationSpec = tween(durationMillis = 300),
    label = "restTextColor"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Menu / Hamburger button on the side
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
      ) {
        Icon(
          imageVector = Icons.Outlined.Menu,
          contentDescription = "القائمة الجانبية",
          tint = extra.textPrimary,
          modifier = Modifier.size(26.dp)
        )
      }

      // Center Brand Title & Subtitle matching Mockup
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "MyOS",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            letterSpacing = 0.5.sp
          ),
          color = extra.textPrimary
        )
        Text(
          text = "عقلك الثاني",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold
          ),
          color = BrandPrimary
        )
      }

      // Action buttons on the opposite side: "راحة" Pill + Search Lens Button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // "راحة" (Rest Day) Pill Button with Coffee Icon
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(restBgColor)
            .border(
              width = 1.dp,
              color = if (isRestModeActive) RestLavenderActive else extra.border,
              shape = RoundedCornerShape(20.dp)
            )
            .clickable(
              role = Role.Button,
              onClick = onRestModeToggle
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.FreeBreakfast,
              contentDescription = "وضع الراحة",
              tint = restTextColor,
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "راحة",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp
              ),
              color = restTextColor
            )
          }
        }

        // Global Search Lens Circular Button
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(extra.secondarySurface)
            .border(1.dp, extra.border, CircleShape)
            .clickable(onClick = onSearchClick),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "البحث الشامل",
            tint = BrandPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
