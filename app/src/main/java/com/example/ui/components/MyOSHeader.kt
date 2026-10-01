package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.RestLavenderActive
import com.example.ui.theme.RestLavenderBg
import com.example.ui.theme.RestLavenderText
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun MyOSHeader(
  isRestModeActive: Boolean,
  onMenuClick: () -> Unit,
  onRestModeToggle: () -> Unit,
  onSearchClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val restBgColor by animateColorAsState(
    targetValue = if (isRestModeActive) RestLavenderActive else RestLavenderBg,
    animationSpec = tween(durationMillis = 300),
    label = "restBgColor"
  )

  val restTextColor by animateColorAsState(
    targetValue = if (isRestModeActive) TextWhite else RestLavenderText,
    animationSpec = tween(durationMillis = 300),
    label = "restTextColor"
  )

  // Status bar safe container
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(BackgroundLight)
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
          tint = TextPrimary,
          modifier = Modifier.size(26.dp)
        )
      }

      // Center Logo and Brand Title
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        MyOSLogoIcon(size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "MyOS",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              letterSpacing = 0.5.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "عقلك الثاني",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            ),
            color = TextSecondary
          )
        }
      }

      // Action buttons: Search magnifying glass and "راحة" (Rest Day) Button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Global Search Lens Button
        IconButton(
          onClick = onSearchClick,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0xFFEFF6FF))
        ) {
          Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "البحث الشامل",
            tint = BrightBlue,
            modifier = Modifier.size(20.dp)
          )
        }

        // "راحة" (Rest Day) Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(restBgColor)
            .clickable(
              role = Role.Button,
              onClick = onRestModeToggle
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
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
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "راحة",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              ),
              color = restTextColor
            )
          }
        }
      }
    }
  }
}
