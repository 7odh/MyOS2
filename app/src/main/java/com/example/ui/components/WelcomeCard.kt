package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.extraColors

@Composable
fun WelcomeCard(
  userName: String,
  motivationalSentence: String,
  isMotivationEnabled: Boolean,
  onEditNameClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val extra = MaterialTheme.extraColors
  val isDark = extra.isDark

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .shadow(
        elevation = if (isDark) 0.dp else 4.dp,
        shape = RoundedCornerShape(24.dp),
        ambientColor = extra.shadow,
        spotColor = extra.shadow
      )
      .clip(RoundedCornerShape(24.dp))
      .border(1.dp, extra.border, RoundedCornerShape(24.dp)),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = extra.cardSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(132.dp)
    ) {
      // 1. Full-Bleed Panoramic Mountain Background Image
      val mountainDrawable = if (isDark) {
        R.drawable.img_card_bg_dark_1790877462295
      } else {
        R.drawable.img_card_bg_light_1790877444456
      }

      Image(
        painter = painterResource(id = mountainDrawable),
        contentDescription = "خلفية قمة الجبل",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
      )

      // 2. Smooth Directional Gradient Overlay
      // In RTL: Right (startX = max) is the text side with high contrast overlay.
      // Left (endX = 0) is the mountain summit side, completely transparent so art bleeds to the edge with no gaps!
      val textOverlay = if (isDark) {
        Brush.horizontalGradient(
          0.0f to Color(0xFF0B1220).copy(alpha = 0.94f),
          0.42f to Color(0xFF0B1220).copy(alpha = 0.72f),
          0.70f to Color.Transparent,
          1.0f to Color.Transparent,
          startX = Float.POSITIVE_INFINITY,
          endX = 0f
        )
      } else {
        Brush.horizontalGradient(
          0.0f to Color(0xFFFFFFFF).copy(alpha = 0.94f),
          0.42f to Color(0xFFFFFFFF).copy(alpha = 0.72f),
          0.70f to Color.Transparent,
          1.0f to Color.Transparent,
          startX = Float.POSITIVE_INFINITY,
          endX = 0f
        )
      }

      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(textOverlay)
      )

      // 3. Foreground Content: Text and Edit Button
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        // Text Column on the Right (RTL Start)
        Column(
          modifier = Modifier
            .fillMaxWidth(0.68f)
            .align(Alignment.CenterStart),
          verticalArrangement = Arrangement.Center
        ) {
          // Greeting Row with Edit Button right alongside
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "مرحباً $userName 👋",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                letterSpacing = 0.2.sp
              ),
              color = extra.textPrimary
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Edit Button circle right next to the greeting
            Box(
              modifier = Modifier
                .size(28.dp)
                .shadow(1.dp, CircleShape)
                .clip(CircleShape)
                .background(if (isDark) Color(0xFF1E293B) else Color(0xFFFFFFFF))
                .border(1.dp, extra.border, CircleShape)
                .clickable(onClick = onEditNameClick),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "تعديل الاسم والمقولة",
                tint = BrandPrimary,
                modifier = Modifier.size(14.dp)
              )
            }
          }

          // Motivational Quote below title
          if (isMotivationEnabled && motivationalSentence.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "\"$motivationalSentence\"",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium
              ),
              color = extra.textSecondary
            )
          }
        }
      }
    }
  }
}
