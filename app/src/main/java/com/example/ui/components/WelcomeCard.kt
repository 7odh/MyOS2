package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WelcomeCard(
  userName: String,
  motivationalSentence: String,
  isMotivationEnabled: Boolean,
  onEditNameClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFFFFFFFF),
              Color(0xFFF8FAFC)
            )
          )
        )
        .padding(horizontal = 20.dp, vertical = 18.dp),
      verticalArrangement = Arrangement.Center
    ) {
      // Greeting Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "مرحباً $userName 👋",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp
            ),
            color = TextPrimary
          )
        }

        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(GoalBlueBg)
            .clickable(onClick = onEditNameClick),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "تعديل الاسم",
            tint = BrightBlue,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      if (isMotivationEnabled && motivationalSentence.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = motivationalSentence,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight.Normal
          ),
          color = TextSecondary
        )
      }
    }
  }
}
