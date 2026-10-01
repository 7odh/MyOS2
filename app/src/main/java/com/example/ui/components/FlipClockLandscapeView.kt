package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AmbientSoundProvider
import com.example.model.AmbientSoundType
import com.example.model.FocusAttachment
import com.example.model.FocusTimerMode
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald

@Composable
fun FlipClockLandscapeView(
  remainingSeconds: Int,
  totalSeconds: Int,
  isRunning: Boolean,
  mode: FocusTimerMode,
  attachment: FocusAttachment,
  selectedSound: AmbientSoundType,
  onTogglePlayPause: () -> Unit,
  onReset: () -> Unit,
  onFinishEarly: () -> Unit,
  onSelectSound: (AmbientSoundType) -> Unit,
  onExitFlipClock: () -> Unit,
  modifier: Modifier = Modifier
) {
  val minutes = remainingSeconds / 60
  val seconds = remainingSeconds % 60

  val minTens = minutes / 10
  val minUnits = minutes % 10
  val secTens = seconds / 10
  val secUnits = seconds % 10

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val colonAlpha by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isRunning) 0.2f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "colonBlink"
  )

  var showSoundPicker by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF090D16))
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 24.dp, vertical = 12.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Bar: Attachment Info & Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Linked Item Chip
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(text = attachment.iconEmoji, fontSize = 16.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = attachment.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFF1F5F9)
          )
          if (attachment.targetMinutes != null && attachment.currentMinutes != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "(${attachment.currentMinutes}/${attachment.targetMinutes} ${attachment.unit ?: "د"})",
              fontSize = 12.sp,
              color = HabitEmerald
            )
          }
        }

        // Mode badge & Sound button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF1E293B))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "${mode.emoji} ${mode.titleArabic}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF94A3B8)
            )
          }

          // Sound selector button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (selectedSound != AmbientSoundType.NONE) Color(0xFF0F766E) else Color(0xFF1E293B))
              .clickable { showSoundPicker = !showSoundPicker }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (selectedSound != AmbientSoundType.NONE) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                contentDescription = "الصوت المحيطي",
                tint = if (selectedSound != AmbientSoundType.NONE) Color(0xFF5EEAD4) else Color(0xFF94A3B8),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              val soundName = when (selectedSound) {
                AmbientSoundType.RAIN -> "🌧️ أمطار"
                AmbientSoundType.OCEAN -> "🌊 بحر"
                AmbientSoundType.FIRE -> "🔥 نار"
                AmbientSoundType.SNOW -> "❄️ ثلج"
                AmbientSoundType.FOREST -> "🌲 غابة"
                AmbientSoundType.NONE -> "صامت"
              }
              Text(
                text = soundName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
              )
            }
          }

          // Exit Landscape / Close
          IconButton(
            onClick = onExitFlipClock,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFF1E293B))
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق وضع ملء الشاشة",
              tint = Color(0xFFE2E8F0),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Middle: Grand Retro Flip Clock Display (Enforced LTR so time is always MM:SS left-to-right)
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Minutes Pair (Left)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FlipDigitCard(digit = minTens)
            FlipDigitCard(digit = minUnits)
          }

          // Pulsing Colon (Center)
          Box(
            modifier = Modifier
              .padding(horizontal = 14.dp)
              .alpha(colonAlpha),
            contentAlignment = Alignment.Center
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF64748B))
              )
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF64748B))
              )
            }
          }

          // Seconds Pair (Right)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FlipDigitCard(digit = secTens)
            FlipDigitCard(digit = secUnits)
          }
        }
      }

      // Bottom Bar: Controls
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Reset Button
        IconButton(
          onClick = onReset,
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E293B))
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "إعادة ضبط",
            tint = Color(0xFF94A3B8)
          )
        }

        Spacer(modifier = Modifier.width(28.dp))

        // Large Play / Pause Button
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onTogglePlayPause() },
          color = if (isRunning) Color(0xFFEF4444) else HabitEmerald,
          shape = RoundedCornerShape(24.dp),
          shadowElevation = 8.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isRunning) "إيقاف مؤقت" else "بدء",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isRunning) "إيقاف مؤقت" else "ابدأ الجلسة",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        Spacer(modifier = Modifier.width(28.dp))

        // Finish Early & Save
        IconButton(
          onClick = onFinishEarly,
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E293B))
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "إنهاء واحتساب الإنجاز",
            tint = HabitEmerald
          )
        }
      }
    }

    // Ambient Sound Popup Sheet/Overlay
    AnimatedVisibility(
      visible = showSoundPicker,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier.align(Alignment.TopEnd)
    ) {
      Surface(
        modifier = Modifier
          .padding(top = 48.dp)
          .clip(RoundedCornerShape(16.dp)),
        color = Color(0xFF1E293B),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "الأصوات المحيطية الطبيعية 🎧",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF94A3B8)
          )

          AmbientSoundProvider.defaultSounds.forEach { sound ->
            val isSelected = selectedSound == sound.type
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFF0F766E) else Color.Transparent)
                .clickable {
                  onSelectSound(sound.type)
                  showSoundPicker = false
                }
                .padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = sound.emoji, fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = sound.nameArabic,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.White
              )
            }
          }

          // Silent option
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (selectedSound == AmbientSoundType.NONE) Color(0xFF334155) else Color.Transparent)
              .clickable {
                onSelectSound(AmbientSoundType.NONE)
                showSoundPicker = false
              }
              .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🔇", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "صامت (بدون صوت)",
              fontSize = 13.sp,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun FlipDigitCard(
  digit: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .width(72.dp)
      .height(110.dp)
      .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
      .clip(RoundedCornerShape(12.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF222B3E),
            Color(0xFF171F2F),
            Color(0xFF101725)
          )
        )
      )
      .border(1.dp, Color(0xFF334155).copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
    contentAlignment = Alignment.Center
  ) {
    // Number text
    Text(
      text = "$digit",
      fontSize = 72.sp,
      fontWeight = FontWeight.Black,
      fontFamily = FontFamily.Monospace,
      color = Color(0xFFF8FAFC)
    )

    // Center horizontal flip slit / divider
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(Color(0xFF070B13))
        .align(Alignment.Center)
    )

    // Subtle side hinge notches
    Box(
      modifier = Modifier
        .size(width = 4.dp, height = 6.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(Color(0xFF090D16))
        .align(Alignment.CenterStart)
    )
    Box(
      modifier = Modifier
        .size(width = 4.dp, height = 6.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(Color(0xFF090D16))
        .align(Alignment.CenterEnd)
    )
  }
}
