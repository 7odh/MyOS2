package com.example.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.outlined.LinkOff
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AmbientSoundProvider
import com.example.model.AmbientSoundType
import com.example.model.FocusAttachType
import com.example.model.FocusAttachment
import com.example.model.FocusTimerMode
import com.example.model.ScreenDestination
import com.example.ui.components.AttachFocusBottomSheet
import com.example.ui.components.CustomDurationDialog
import com.example.ui.components.FlipClockLandscapeView
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.MyOSHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.viewmodel.MyOSUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onToggleRestMode: () -> Unit,
  onSetFocusMode: (FocusTimerMode) -> Unit,
  onSetFocusDurationMinutes: (Int) -> Unit,
  onStartTimer: () -> Unit,
  onPauseTimer: () -> Unit,
  onResetTimer: () -> Unit,
  onFinishEarly: () -> Unit,
  onSetAttachment: (FocusAttachment) -> Unit,
  onClearAttachment: () -> Unit,
  onSelectSound: (AmbientSoundType) -> Unit,
  onToggleAttachPicker: (Boolean) -> Unit,
  onToggleCustomDurationDialog: (Boolean) -> Unit,
  onToggleFlipClockFullScreen: (Boolean) -> Unit,
  onDismissNotification: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    if (uiState.isFlipClockFullScreen) {
      onToggleFlipClockFullScreen(false)
    } else {
      onScreenSelected(ScreenDestination.HOME)
    }
  }

  val configuration = LocalConfiguration.current
  val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

  // Full-Screen Landscape Flip Clock (like TickTick)
  if (isLandscape || uiState.isFlipClockFullScreen) {
    FlipClockLandscapeView(
      remainingSeconds = uiState.focusRemainingSeconds,
      totalSeconds = uiState.focusTotalSeconds,
      isRunning = uiState.isFocusTimerRunning,
      mode = uiState.focusTimerMode,
      attachment = uiState.focusAttachment,
      selectedSound = uiState.selectedAmbientSound,
      onTogglePlayPause = {
        if (uiState.isFocusTimerRunning) onPauseTimer() else onStartTimer()
      },
      onReset = onResetTimer,
      onFinishEarly = onFinishEarly,
      onSelectSound = onSelectSound,
      onExitFlipClock = { onToggleFlipClockFullScreen(false) }
    )
    return
  }

  // Portrait Mode
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      onDismissNotification()
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(
        drawerContainerColor = SurfaceWhite,
        modifier = Modifier.width(280.dp)
      ) {
        NavigationDrawerContent(
          currentScreen = ScreenDestination.FOCUS,
          onScreenSelected = onScreenSelected,
          onCloseDrawer = { scope.launch { drawerState.close() } }
        )
      }
    }
  ) {
    Scaffold(
      containerColor = BackgroundLight,
      snackbarHost = { SnackbarHost(snackbarHostState) },
      topBar = {
        MyOSHeader(
          isRestModeActive = uiState.user.isRestModeActive,
          onMenuClick = { scope.launch { drawerState.open() } },
          onRestModeToggle = onToggleRestMode,
          onSearchClick = { onScreenSelected(ScreenDestination.SEARCH) }
        )
      },
      bottomBar = {
        MyOSBottomNavigationBar(
          currentScreen = ScreenDestination.FOCUS,
          onTabSelected = onScreenSelected,
          onMoreClick = { scope.launch { drawerState.open() } }
        )
      },
      modifier = modifier
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 10.dp)
          .widthIn(max = 600.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Quick Toolbar: Flip Clock Fullscreen Toggle & Mode Description
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "اختر نمط الجلسة والمدة المطلوبة",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
          )

          // Flip Clock Button
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable { onToggleFlipClockFullScreen(true) },
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ScreenRotation,
                contentDescription = "ساعة فليب شاشة كاملة",
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ساعة فليب 🔄",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Timer Mode Tabs (Pomodoro, Short Break, Long Break, Custom)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFE2E8F0))
            .padding(4.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          FocusTimerMode.values().forEach { mode ->
            val isSelected = uiState.focusTimerMode == mode
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) SurfaceWhite else Color.Transparent)
                .clickable { onSetFocusMode(mode) }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = mode.emoji, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = mode.titleArabic,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) TextPrimary else TextSecondary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Preset Durations Strip
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(15, 25, 30, 45, 60).forEach { mins ->
            val isCurrent = uiState.focusTotalSeconds == mins * 60
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isCurrent) BrightBlue else SurfaceWhite)
                .border(1.dp, if (isCurrent) BrightBlue else BorderLight, RoundedCornerShape(10.dp))
                .clickable { onSetFocusDurationMinutes(mins) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = "$mins دقيقة",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) Color.White else TextPrimary
              )
            }
          }

          // Custom Duration Button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(GoalBlueBg)
              .border(1.dp, BrightBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
              .clickable { onToggleCustomDurationDialog(true) }
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                tint = BrightBlue,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "+ وقت مخصص",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BrightBlue
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Linked Attachment Card
        val attachment = uiState.focusAttachment
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggleAttachPicker(true) },
          colors = CardDefaults.cardColors(
            containerColor = when (attachment.type) {
              FocusAttachType.HABIT -> HabitEmeraldBg
              FocusAttachType.GENERAL_TASK -> TaskVioletBg
              FocusAttachType.GOAL_TASK -> GoalBlueBg
              FocusAttachType.NONE -> SurfaceWhite
            }
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when (attachment.type) {
              FocusAttachType.HABIT -> HabitEmerald.copy(alpha = 0.4f)
              FocusAttachType.GENERAL_TASK -> TaskViolet.copy(alpha = 0.4f)
              FocusAttachType.GOAL_TASK -> BrightBlue.copy(alpha = 0.4f)
              FocusAttachType.NONE -> BorderLight
            }
          ),
          shape = RoundedCornerShape(16.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                  when (attachment.type) {
                    FocusAttachType.HABIT -> HabitEmerald
                    FocusAttachType.GENERAL_TASK -> TaskViolet
                    FocusAttachType.GOAL_TASK -> BrightBlue
                    FocusAttachType.NONE -> Color(0xFFF1F5F9)
                  }
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(text = attachment.iconEmoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = when (attachment.type) {
                    FocusAttachType.HABIT -> "عادة مرتبطة 🌱"
                    FocusAttachType.GENERAL_TASK -> "مهمة عامة ⚡"
                    FocusAttachType.GOAL_TASK -> "مهمة هدف 🎯"
                    FocusAttachType.NONE -> "جلسة حرة بدون ربط"
                  },
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (attachment.type) {
                    FocusAttachType.HABIT -> HabitEmerald
                    FocusAttachType.GENERAL_TASK -> TaskViolet
                    FocusAttachType.GOAL_TASK -> BrightBlue
                    FocusAttachType.NONE -> TextMuted
                  }
                )
                if (attachment.isTimedHabit) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "⏱️ موقوتة",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                  )
                }
              }

              Text(
                text = attachment.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )

              if (attachment.targetMinutes != null && attachment.currentMinutes != null) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                  progress = {
                    if (attachment.targetMinutes > 0) {
                      (attachment.currentMinutes.toFloat() / attachment.targetMinutes.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                  color = HabitEmerald,
                  trackColor = HabitEmerald.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "المسجل اليوم: ${attachment.currentMinutes} من ${attachment.targetMinutes} ${attachment.unit ?: "دقيقة"}",
                  fontSize = 11.sp,
                  color = HabitEmerald,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              if (attachment.type != FocusAttachType.NONE) {
                IconButton(
                  onClick = onClearAttachment,
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "فك الارتباط",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
              IconButton(
                onClick = { onToggleAttachPicker(true) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Link,
                  contentDescription = "تغيير الارتباط",
                  tint = BrightBlue,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grand Circular Timer Display
        val minutes = uiState.focusRemainingSeconds / 60
        val seconds = uiState.focusRemainingSeconds % 60
        val timeFormatted = "%02d:%02d".format(minutes, seconds)

        val progress = if (uiState.focusTotalSeconds > 0) {
          (uiState.focusRemainingSeconds.toFloat() / uiState.focusTotalSeconds.toFloat()).coerceIn(0f, 1f)
        } else 0f
        val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

        Box(
          modifier = Modifier.size(260.dp),
          contentAlignment = Alignment.Center
        ) {
          // Circular Progress Arc Canvas
          Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
              color = Color(0xFFE2E8F0),
              startAngle = -90f,
              sweepAngle = 360f,
              useCenter = false,
              topLeft = topLeft,
              size = arcSize,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Progress Gradient Arc
            val primaryColor = if (uiState.focusTimerMode == FocusTimerMode.POMODORO) Color(0xFFEF4444) else BrightBlue
            val secondaryColor = if (uiState.focusTimerMode == FocusTimerMode.POMODORO) Color(0xFFF97316) else HabitEmerald

            drawArc(
              brush = Brush.sweepGradient(
                colors = listOf(primaryColor, secondaryColor, primaryColor)
              ),
              startAngle = -90f,
              sweepAngle = animatedProgress * 360f,
              useCenter = false,
              topLeft = topLeft,
              size = arcSize,
              style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
          }

          // Center Time and Status Info
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
              Text(
                text = timeFormatted,
                fontSize = 54.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                  if (uiState.isFocusTimerRunning) HabitEmeraldBg else GoalBlueBg
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Text(
                text = if (uiState.isFocusTimerRunning) {
                  "جاري التركيز... استمر! 🧘‍♂️"
                } else if (uiState.focusRemainingSeconds < uiState.focusTotalSeconds) {
                  "مؤقت متوقف مؤقتاً ⏸️"
                } else {
                  "جاهز لبدء الجلسة 🚀"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isFocusTimerRunning) HabitEmerald else BrightBlue
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Playback Action Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Reset Button
          IconButton(
            onClick = onResetTimer,
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(SurfaceWhite)
              .border(1.dp, BorderLight, CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "إعادة ضبط",
              tint = TextSecondary,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(20.dp))

          // Main Play / Pause Button
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(28.dp))
              .clickable {
                if (uiState.isFocusTimerRunning) onPauseTimer() else onStartTimer()
              }
              .shadow(10.dp, RoundedCornerShape(28.dp)),
            color = if (uiState.isFocusTimerRunning) Color(0xFFEF4444) else HabitEmerald,
            shape = RoundedCornerShape(28.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 36.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (uiState.isFocusTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (uiState.isFocusTimerRunning) "إيقاف مؤقت" else "ابدأ الجلسة",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = if (uiState.isFocusTimerRunning) "إيقاف مؤقت" else "ابدأ التركيز",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          Spacer(modifier = Modifier.width(20.dp))

          // Finish Early & Record Progress Button
          IconButton(
            onClick = onFinishEarly,
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(SurfaceWhite)
              .border(1.dp, HabitEmerald.copy(alpha = 0.5f), CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "إنهاء واحتساب الإنجاز",
              tint = HabitEmerald,
              modifier = Modifier.size(24.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Natural Ambient Sounds Strip
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🎧", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "أصوات الطبيعة المحيطية",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              }
              if (uiState.selectedAmbientSound != AmbientSoundType.NONE) {
                Text(
                  text = "قيد التشغيل 🔊",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = HabitEmerald
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sound selection chips
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Silent chip
              val isSilent = uiState.selectedAmbientSound == AmbientSoundType.NONE
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSilent) Color(0xFFE2E8F0) else Color(0xFFF8FAFC))
                  .clickable { onSelectSound(AmbientSoundType.NONE) }
                  .padding(horizontal = 12.dp, vertical = 8.dp)
              ) {
                Text(
                  text = "🔇 صامت",
                  fontSize = 12.sp,
                  fontWeight = if (isSilent) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSilent) TextPrimary else TextSecondary
                )
              }

              AmbientSoundProvider.defaultSounds.forEach { sound ->
                val isSelected = uiState.selectedAmbientSound == sound.type
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) Color(0xFF0F766E) else Color(0xFFF8FAFC))
                    .border(
                      1.dp,
                      if (isSelected) Color(0xFF0F766E) else BorderLight,
                      RoundedCornerShape(10.dp)
                    )
                    .clickable { onSelectSound(sound.type) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = sound.emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = sound.nameArabic,
                      fontSize = 12.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isSelected) Color.White else TextPrimary
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Focus Stats Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "إحصائيات التركيز اليومية 📊",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Sessions Completed Box
              Column(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(GoalBlueBg)
                  .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "${uiState.focusSessionsCompletedToday}",
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Black,
                  color = BrightBlue
                )
                Text(
                  text = "جلسات مكتملة اليوم",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }

              // Total Minutes Box
              Column(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(HabitEmeraldBg)
                  .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "${uiState.focusTotalMinutesToday}",
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Black,
                  color = HabitEmerald
                )
                Text(
                  text = "دقيقة تركيز عميق",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "💡 \"التركيز هو فن إقصاء المشتتات والعمل بعمق على الأهم.\"",
              fontSize = 11.sp,
              color = TextMuted,
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Bottom Sheets & Dialogs
  if (uiState.isAttachPickerVisible) {
    AttachFocusBottomSheet(
      currentAttachment = uiState.focusAttachment,
      habits = uiState.habits,
      tasks = uiState.generalTasks,
      goals = uiState.goals,
      onSelectAttachment = onSetAttachment,
      onClearAttachment = onClearAttachment,
      onDismiss = { onToggleAttachPicker(false) }
    )
  }

  if (uiState.isCustomDurationDialogVisible) {
    CustomDurationDialog(
      initialMinutes = uiState.focusTotalSeconds / 60,
      onConfirm = onSetFocusDurationMinutes,
      onDismiss = { onToggleCustomDurationDialog(false) }
    )
  }
}
