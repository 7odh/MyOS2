package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalendarViewMode
import com.example.model.DayHabitRecord
import com.example.model.DaySummaryHistory
import com.example.model.DayTaskRecord
import com.example.model.Priority
import com.example.model.ScreenDestination
import com.example.model.getTodayDateString
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onSelectDate: (String) -> Unit,
  onChangeViewMode: (CalendarViewMode) -> Unit,
  onToggleTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit,
  onPostponeGoalTask: (String, String) -> Unit,
  onIncrementHabit: (String) -> Unit,
  onDismissNotification: () -> Unit,
  modifier: Modifier = Modifier
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      onDismissNotification()
    }
  }

  // Current calendar viewing month and year
  var displayYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
  var displayMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) } // 0-based

  val selectedDate = uiState.selectedCalendarDate
  val daySummary = uiState.selectedDaySummary

  val arabicMonths = listOf(
    "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
    "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
  )

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(drawerContainerColor = SurfaceWhite) {
        NavigationDrawerContent(
          currentScreen = ScreenDestination.CALENDAR,
          onScreenSelected = onScreenSelected,
          onCloseDrawer = { scope.launch { drawerState.close() } }
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        Surface(
          color = SurfaceWhite,
          shadowElevation = 1.dp
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp, bottom = 4.dp)
          ) {
            // Header Row: Menu, Title & Month Navigation, Today Button
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { scope.launch { drawerState.open() } },
                  modifier = Modifier.size(38.dp)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "القائمة",
                    tint = TextPrimary
                  )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Month / Year Title
                Text(
                  text = "${arabicMonths[displayMonth]} $displayYear",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                  ),
                  color = TextPrimary
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Navigation buttons (Previous & Next)
                IconButton(
                  onClick = {
                    if (displayMonth == 0) {
                      displayMonth = 11
                      displayYear -= 1
                    } else {
                      displayMonth -= 1
                    }
                  },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "الشهر السابق",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = {
                    if (displayMonth == 11) {
                      displayMonth = 0
                      displayYear += 1
                    } else {
                      displayMonth += 1
                    }
                  },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "الشهر التالي",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }

              // Search and "اليوم" Quick Shortcut Buttons
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { onScreenSelected(ScreenDestination.SEARCH) },
                  modifier = Modifier.size(36.dp)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "البحث الشامل",
                    tint = BrightBlue,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(4.dp))

                TextButton(
                  onClick = {
                    val todayCal = Calendar.getInstance()
                    displayYear = todayCal.get(Calendar.YEAR)
                    displayMonth = todayCal.get(Calendar.MONTH)
                    onSelectDate(getTodayDateString())
                  },
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoalBlueBg)
                ) {
                  Text(
                    text = "اليوم ⚡",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    ),
                    color = BrightBlue
                  )
                }
              }
            }

            // Mode Selector: الشهر | الأسبوع | الأجندة (TickTick Style View Modes)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              CalendarViewMode.values().forEach { mode ->
                val isSelected = uiState.calendarViewMode == mode
                FilterChip(
                  selected = isSelected,
                  onClick = { onChangeViewMode(mode) },
                  label = {
                    Text(
                      text = "${mode.iconText} ${mode.titleArabic}",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                      )
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrightBlue,
                    selectedLabelColor = TextWhite,
                    containerColor = Color(0xFFF1F5F9),
                    labelColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(20.dp),
                  border = null,
                  modifier = Modifier.height(32.dp)
                )
              }
            }
          }
        }
      },
      bottomBar = {
        MyOSBottomNavigationBar(
          currentScreen = ScreenDestination.CALENDAR,
          onTabSelected = onScreenSelected,
          onMoreClick = { scope.launch { drawerState.open() } }
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) },
      containerColor = BackgroundLight,
      modifier = modifier
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 640.dp)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Spacer(modifier = Modifier.height(6.dp))

          // 1. Calendar View Component (Month / Week / Agenda)
          when (uiState.calendarViewMode) {
            CalendarViewMode.MONTH -> {
              MonthCalendarView(
                year = displayYear,
                month = displayMonth,
                selectedDate = selectedDate,
                todayDate = getTodayDateString(),
                onDateSelected = onSelectDate
              )
            }
            CalendarViewMode.WEEK -> {
              WeekCalendarView(
                selectedDate = selectedDate,
                todayDate = getTodayDateString(),
                onDateSelected = onSelectDate
              )
            }
            CalendarViewMode.AGENDA -> {
              AgendaQuickPicker(
                selectedDate = selectedDate,
                todayDate = getTodayDateString(),
                onDateSelected = onSelectDate
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 2. Selected Day Detail Section (مرتبط بكل شيء في التطبيق في هذا اليوم)
          if (daySummary != null) {
            DayDetailSection(
              summary = daySummary,
              onToggleTask = onToggleTask,
              onToggleGoalTask = onToggleGoalTask,
              onPostponeTask = onPostponeTask,
              onPostponeGoalTask = onPostponeGoalTask,
              onIncrementHabit = onIncrementHabit
            )
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

// ---------------------------------------------------------
// Month Calendar Grid View
// ---------------------------------------------------------
@Composable
private fun MonthCalendarView(
  year: Int,
  month: Int,
  selectedDate: String,
  todayDate: String,
  onDateSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      // Weekday Header Row (Saturday to Friday in Arabic)
      val weekDays = listOf("سبت", "أحد", "إثنين", "ثلاثاء", "أربعاء", "خميس", "جمعة")
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        weekDays.forEach { dayName ->
          Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), thickness = 0.5.dp)
      Spacer(modifier = Modifier.height(6.dp))

      // Month days grid calculation
      val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
      }

      // In Arabic calendar convention starting Saturday:
      // Calendar.SATURDAY = 7 -> index 0, SUNDAY = 1 -> index 1, ... FRIDAY = 6 -> index 6
      val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
      val leadingEmptySlots = when (dayOfWeek) {
        Calendar.SATURDAY -> 0
        Calendar.SUNDAY -> 1
        Calendar.MONDAY -> 2
        Calendar.TUESDAY -> 3
        Calendar.WEDNESDAY -> 4
        Calendar.THURSDAY -> 5
        Calendar.FRIDAY -> 6
        else -> 0
      }

      val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
      val totalSlots = ((leadingEmptySlots + maxDaysInMonth + 6) / 7) * 7

      val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)

      // Rows of weeks
      val weeksCount = totalSlots / 7
      for (weekIndex in 0 until weeksCount) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          for (dayCol in 0 until 7) {
            val slotIndex = weekIndex * 7 + dayCol
            val dayNumber = slotIndex - leadingEmptySlots + 1

            if (dayNumber in 1..maxDaysInMonth) {
              val dateCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayNumber)
              }
              val dateString = sdf.format(dateCal.time)
              val isSelected = dateString == selectedDate
              val isToday = dateString == todayDate

              CalendarDayCell(
                dayNumber = dayNumber,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onDateSelected(dateString) },
                modifier = Modifier.weight(1f)
              )
            } else {
              // Empty slot for padding
              Box(
                modifier = Modifier
                  .weight(1f)
                  .aspectRatio(1f)
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      // Legend row
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        LegendDot(color = HabitEmerald, label = "عادات")
        Spacer(modifier = Modifier.width(12.dp))
        LegendDot(color = TaskViolet, label = "مهام عامة")
        Spacer(modifier = Modifier.width(12.dp))
        LegendDot(color = GoalBlue, label = "مهام أهداف")
      }
    }
  }
}

@Composable
private fun CalendarDayCell(
  dayNumber: Int,
  isSelected: Boolean,
  isToday: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgCol = when {
    isSelected -> BrightBlue
    isToday -> GoalBlueBg
    else -> Color.Transparent
  }
  val textCol = when {
    isSelected -> TextWhite
    isToday -> BrightBlue
    else -> TextPrimary
  }

  Box(
    modifier = modifier
      .aspectRatio(1f)
      .padding(2.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(bgCol)
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = dayNumber.toString(),
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
          fontSize = 13.sp
        ),
        color = textCol
      )

      // Activity indicator dots
      Row(
        modifier = Modifier.padding(top = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        Box(
          modifier = Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(if (isSelected) TextWhite.copy(alpha = 0.8f) else HabitEmerald)
        )
        Box(
          modifier = Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(if (isSelected) TextWhite.copy(alpha = 0.8f) else TaskViolet)
        )
      }
    }
  }
}

@Composable
private fun LegendDot(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(6.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      color = TextSecondary
    )
  }
}

// ---------------------------------------------------------
// Week Calendar View (Horizontal Strip)
// ---------------------------------------------------------
@Composable
private fun WeekCalendarView(
  selectedDate: String,
  todayDate: String,
  onDateSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
  val cal = Calendar.getInstance()
  try {
    cal.time = sdf.parse(selectedDate) ?: Date()
  } catch (_: Exception) {}

  // Find start of week (Saturday)
  val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
  val daysBack = when (currentDayOfWeek) {
    Calendar.SATURDAY -> 0
    Calendar.SUNDAY -> 1
    Calendar.MONDAY -> 2
    Calendar.TUESDAY -> 3
    Calendar.WEDNESDAY -> 4
    Calendar.THURSDAY -> 5
    Calendar.FRIDAY -> 6
    else -> 0
  }
  cal.add(Calendar.DAY_OF_YEAR, -daysBack)

  val weekDaysArabic = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      for (i in 0 until 7) {
        val dateStr = sdf.format(cal.time)
        val dayNum = cal.get(Calendar.DAY_OF_MONTH)
        val dayName = weekDaysArabic[i]
        val isSelected = dateStr == selectedDate
        val isToday = dateStr == todayDate

        val bgCol = when {
          isSelected -> BrightBlue
          isToday -> GoalBlueBg
          else -> Color(0xFFF8FAFC)
        }
        val textCol = when {
          isSelected -> TextWhite
          isToday -> BrightBlue
          else -> TextPrimary
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 3.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgCol)
            .clickable { onDateSelected(dateStr) }
            .padding(vertical = 10.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = dayName,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              ),
              color = if (isSelected) TextWhite.copy(alpha = 0.9f) else TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = dayNum.toString(),
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              ),
              color = textCol
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Indicators
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              Box(
                modifier = Modifier
                  .size(4.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) TextWhite else HabitEmerald)
              )
              Box(
                modifier = Modifier
                  .size(4.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) TextWhite else TaskViolet)
              )
            }
          }
        }

        cal.add(Calendar.DAY_OF_YEAR, 1)
      }
    }
  }
}

// ---------------------------------------------------------
// Agenda View: Quick Horizontal Day Selector
// ---------------------------------------------------------
@Composable
private fun AgendaQuickPicker(
  selectedDate: String,
  todayDate: String,
  onDateSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
  val list = remember {
    val items = mutableListOf<String>()
    for (offset in -4..5) {
      val c = Calendar.getInstance()
      c.add(Calendar.DAY_OF_YEAR, offset)
      items.add(sdf.format(c.time))
    }
    items
  }

  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(list) { dKey ->
      val isSelected = dKey == selectedDate
      val isToday = dKey == todayDate

      val bgCol = when {
        isSelected -> BrightBlue
        isToday -> GoalBlueBg
        else -> SurfaceCard
      }
      val textCol = when {
        isSelected -> TextWhite
        isToday -> BrightBlue
        else -> TextPrimary
      }

      val displayLabel = when (dKey) {
        todayDate -> "اليوم ⚡"
        com.example.data.getDateOffsetKey(-1) -> "أمس"
        com.example.data.getDateOffsetKey(1) -> "غداً"
        else -> dKey.substring(5) // MM/dd
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(bgCol)
          .border(
            width = 1.dp,
            color = if (isSelected) BrightBlue else BorderLight,
            shape = RoundedCornerShape(16.dp)
          )
          .clickable { onDateSelected(dKey) }
          .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = displayLabel,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            ),
            color = textCol
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = dKey,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (isSelected) TextWhite.copy(alpha = 0.8f) else TextSecondary
          )
        }
      }
    }
  }
}

// ---------------------------------------------------------
// Selected Day Detail Section (Comprehensive Records)
// ---------------------------------------------------------
@Composable
private fun DayDetailSection(
  summary: DaySummaryHistory,
  onToggleTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit,
  onPostponeGoalTask: (String, String) -> Unit,
  onIncrementHabit: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
  ) {
    // 1. Selected Day Banner Card & Overall Completion Metric
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceCard),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Date Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = summary.dateDisplayArabic,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              ),
              color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = summary.dateKey,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = TextSecondary
            )
          }

          // Status Badge (اليوم / سجل تاريخي / قادم)
          val (badgeText, badgeBg, badgeColor) = when {
            summary.isToday -> Triple("اليوم الحالي ⚡", GoalBlueBg, BrightBlue)
            summary.isPast -> Triple("أرشيف السجلات 📜", Color(0xFFFEF3C7), Color(0xFFB45309))
            else -> Triple("مستقبل مجدول ⏳", Color(0xFFF1F5F9), TextSecondary)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(badgeBg)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = badgeText,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              ),
              color = badgeColor
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Completion Rate Metric (نسبة تحقيق المطلوب لليوم)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "نسبة تحقيق المطلوب:",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              ),
              color = TextPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${summary.completionRatePercentage}%",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              ),
              color = if (summary.completionRatePercentage >= 75) HabitEmerald else Color(0xFFD97706)
            )
          }

          Text(
            text = "${summary.completedItemsCount} من ${summary.totalItemsCount} مكتمل",
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp
            ),
            color = TextSecondary
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Bar
        LinearProgressIndicator(
          progress = { summary.completionRatePercentage / 100f },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = if (summary.completionRatePercentage >= 75) HabitEmerald else BrightBlue,
          trackColor = Color(0xFFF1F5F9),
          strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Stats Badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          StatBadge(
            label = "مكتمل",
            count = summary.completedItemsCount,
            bgColor = HabitEmeraldBg,
            textColor = HabitEmerald,
            modifier = Modifier.weight(1f)
          )
          if (summary.missedItemsCount > 0) {
            StatBadge(
              label = "فاتك / لم يُنجز",
              count = summary.missedItemsCount,
              bgColor = Color(0xFFFEF2F2),
              textColor = Color(0xFFEF4444),
              modifier = Modifier.weight(1f)
            )
          }
          if (summary.postponedTasksCount > 0) {
            StatBadge(
              label = "مُرحّل",
              count = summary.postponedTasksCount,
              bgColor = Color(0xFFFFFBEB),
              textColor = Color(0xFFD97706),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // 2. Habits Sub-Section for Selected Day (تفاصيل العادات المحققة والمطلوبة)
    DayHabitsList(
      habits = summary.habits,
      isToday = summary.isToday,
      onIncrement = onIncrementHabit
    )

    Spacer(modifier = Modifier.height(14.dp))

    // 3. General Tasks Sub-Section for Selected Day
    DayGeneralTasksList(
      tasks = summary.tasks.filter { !it.isGoalTask },
      isToday = summary.isToday,
      onToggle = onToggleTask,
      onPostpone = onPostponeTask
    )

    Spacer(modifier = Modifier.height(14.dp))

    // 4. Goal Tasks Sub-Section for Selected Day (المهام المرتبطة بهدف)
    DayGoalTasksList(
      goalTasks = summary.tasks.filter { it.isGoalTask },
      isToday = summary.isToday,
      onToggle = onToggleGoalTask,
      onPostpone = onPostponeGoalTask
    )
  }
}

@Composable
private fun StatBadge(
  label: String,
  count: Int,
  bgColor: Color,
  textColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(bgColor)
      .padding(vertical = 6.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = count.toString(),
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        ),
        color = textColor
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 9.sp,
          fontWeight = FontWeight.Medium
        ),
        color = textColor
      )
    }
  }
}

// ---------------------------------------------------------
// Day Habits List
// ---------------------------------------------------------
@Composable
private fun DayHabitsList(
  habits: List<DayHabitRecord>,
  isToday: Boolean,
  onIncrement: (String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(HabitEmeraldBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Spa,
              contentDescription = "العادات",
              tint = HabitEmerald,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "العادات في هذا اليوم",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            ),
            color = HabitEmerald
          )
        }

        Text(
          text = "${habits.count { it.isCompleted }} من ${habits.size} مكتملة",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
          ),
          color = HabitEmerald
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (habits.isEmpty()) {
        Text(
          text = "لا توجد عادات مجدولة لهذا اليوم",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = TextMuted,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      } else {
        habits.forEach { habit ->
          DayHabitRow(
            habit = habit,
            isToday = isToday,
            onIncrement = { onIncrement(habit.habitId) }
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }
  }
}

@Composable
private fun DayHabitRow(
  habit: DayHabitRecord,
  isToday: Boolean,
  onIncrement: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (habit.isCompleted) Color(0xFFF9FDFB) else Color(0xFFF8FAFC))
      .border(
        width = 1.dp,
        color = if (habit.isCompleted) HabitEmerald.copy(alpha = 0.3f) else BorderLight.copy(alpha = 0.6f),
        shape = RoundedCornerShape(14.dp)
      )
      .padding(10.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Emoji icon
        Text(
          text = habit.iconEmoji,
          fontSize = 18.sp,
          modifier = Modifier.padding(end = 8.dp)
        )

        // Title and notes
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = habit.title,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            ),
            color = TextPrimary
          )

          if (!habit.statusNote.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = habit.statusNote,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              ),
              color = if (habit.isCompleted) HabitEmerald else Color(0xFFB45309)
            )
          }
        }

        // Values & Achievement status
        Text(
          text = "${habit.actualValue} / ${habit.targetValue} ${habit.unit}",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          ),
          color = if (habit.isCompleted) HabitEmerald else TextSecondary
        )

        if (isToday && !habit.isCompleted) {
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(HabitEmeraldBg)
              .clickable(onClick = onIncrement)
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(
              text = "+1",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              ),
              color = HabitEmerald
            )
          }
        }
      }

      // Progress line
      Spacer(modifier = Modifier.height(6.dp))
      LinearProgressIndicator(
        progress = { habit.progressPercentage / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = if (habit.isCompleted) HabitEmerald else Color(0xFFD97706),
        trackColor = Color(0xFFE2E8F0)
      )
    }
  }
}

// ---------------------------------------------------------
// Day General Tasks List
// ---------------------------------------------------------
@Composable
private fun DayGeneralTasksList(
  tasks: List<DayTaskRecord>,
  isToday: Boolean,
  onToggle: (String) -> Unit,
  onPostpone: (String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(TaskVioletBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.CheckCircle,
              contentDescription = "المهام",
              tint = TaskViolet,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "المهام العامة",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            ),
            color = TaskViolet
          )
        }

        Text(
          text = "${tasks.count { it.task.isCompleted }} من ${tasks.size} مكتملة",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
          ),
          color = TaskViolet
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (tasks.isEmpty()) {
        Text(
          text = "لا توجد مهام عامة في هذا اليوم",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = TextMuted,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      } else {
        tasks.forEach { item ->
          DayTaskCardItem(
            record = item,
            isToday = isToday,
            onToggle = { onToggle(item.task.id) },
            onPostpone = { onPostpone(item.task.id) }
          )
          Spacer(modifier = Modifier.height(6.dp))
        }
      }
    }
  }
}

// ---------------------------------------------------------
// Day Goal Tasks List
// ---------------------------------------------------------
@Composable
private fun DayGoalTasksList(
  goalTasks: List<DayTaskRecord>,
  isToday: Boolean,
  onToggle: (String, String) -> Unit,
  onPostpone: (String, String) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(GoalBlueBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.TrackChanges,
              contentDescription = "مهام الأهداف",
              tint = GoalBlue,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "المهام المرتبطة بأهداف",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            ),
            color = GoalBlue
          )
        }

        Text(
          text = "${goalTasks.count { it.task.isCompleted }} من ${goalTasks.size} مكتملة",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
          ),
          color = GoalBlue
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (goalTasks.isEmpty()) {
        Text(
          text = "لا توجد مهام أهداف مجدولة في هذا اليوم",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = TextMuted,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      } else {
        goalTasks.forEach { item ->
          val goalId = item.goalId ?: ""
          DayTaskCardItem(
            record = item,
            isToday = isToday,
            onToggle = { onToggle(goalId, item.task.id) },
            onPostpone = { onPostpone(goalId, item.task.id) }
          )
          Spacer(modifier = Modifier.height(6.dp))
        }
      }
    }
  }
}

@Composable
private fun DayTaskCardItem(
  record: DayTaskRecord,
  isToday: Boolean,
  onToggle: () -> Unit,
  onPostpone: () -> Unit
) {
  val task = record.task
  val isCompleted = task.isCompleted

  val checkBgColor by animateColorAsState(
    targetValue = if (isCompleted) HabitEmerald else Color.Transparent,
    label = "taskCheckBg"
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(if (isCompleted) Color(0xFFF9FDFB) else Color(0xFFF8FAFC))
      .border(
        width = 1.dp,
        color = if (isCompleted) HabitEmerald.copy(alpha = 0.3f) else BorderLight.copy(alpha = 0.6f),
        shape = RoundedCornerShape(12.dp)
      )
      .padding(horizontal = 10.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Checkbox circle
      Box(
        modifier = Modifier
          .size(22.dp)
          .clip(CircleShape)
          .background(checkBgColor)
          .border(1.5.dp, if (isCompleted) HabitEmerald else BorderLight, CircleShape)
          .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
      ) {
        if (isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "مكتملة",
            tint = TextWhite,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Title & Goal Badge
      Column(modifier = Modifier.weight(1f)) {
        if (record.isGoalTask && !record.goalTitle.isNullOrBlank()) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(GoalBlueBg)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "🎯 ${record.goalTitle}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              ),
              color = GoalBlue
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
        }

        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = if (isCompleted) FontWeight.Normal else FontWeight.Medium,
            fontSize = 13.sp,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else null
          ),
          color = if (isCompleted) TextSecondary.copy(alpha = 0.7f) else TextPrimary
        )

        if (!task.notes.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = task.notes,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Postpone Action or Postponed Badge
      if (isToday && !isCompleted) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEFF6FF))
            .clickable(onClick = onPostpone)
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Text(
            text = "ترحيل للغد ➡️",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.SemiBold
            ),
            color = BrightBlue
          )
        }
      } else if (task.isPostponed) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFFBEB))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "مُرحّلة ➡️",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            ),
            color = Color(0xFFD97706)
          )
        }
      }
    }
  }
}
