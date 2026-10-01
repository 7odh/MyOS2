package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Goal
import com.example.model.ScreenDestination
import com.example.model.SearchCategory
import com.example.model.SearchResultItem
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MyOSUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
  uiState: MyOSUiState,
  onQueryChanged: (String) -> Unit,
  onCategorySelected: (SearchCategory) -> Unit,
  onExecuteSearch: (String) -> Unit,
  onClearQuery: () -> Unit,
  onClearRecentSearches: () -> Unit,
  onNavigateBack: () -> Unit,
  onSelectGoal: (String) -> Unit,
  onSelectCalendarDate: (String) -> Unit,
  onToggleGeneralTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit,
  onPostponeGoalTask: (String, String) -> Unit,
  onIncrementHabit: (String) -> Unit,
  onToggleHabitBoolean: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onNavigateBack()
  }

  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  val searchResults = uiState.searchResults
  val query = uiState.globalSearchQuery

  Scaffold(
    topBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(SurfaceWhite)
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        // Search Input Bar with Back Arrow
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "الرجوع",
              tint = TextPrimary
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = {
              Text(
                text = "ابحث في كل شيء (مهام، عادات، أهداف، أفكار)...",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "بحث",
                tint = BrightBlue,
                modifier = Modifier.size(20.dp)
              )
            },
            trailingIcon = {
              if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "مسح",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onExecuteSearch(query) }),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFFF8FAFC),
              unfocusedContainerColor = Color(0xFFF8FAFC),
              focusedBorderColor = BrightBlue,
              unfocusedBorderColor = BorderLight
            ),
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
              .focusRequester(focusRequester)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          SearchCategory.entries.forEach { cat ->
            val isSelected = cat == uiState.activeSearchCategory
            FilterChip(
              selected = isSelected,
              onClick = { onCategorySelected(cat) },
              label = {
                Text(
                  text = "${cat.iconEmoji} ${cat.titleArabic}",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = GoalBlueBg,
                selectedLabelColor = BrightBlue,
                containerColor = SurfaceWhite,
                labelColor = TextPrimary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = if (isSelected) BrightBlue else BorderLight,
                enabled = true,
                selected = isSelected
              )
            )
          }
        }
      }
    },
    containerColor = BackgroundLight,
    modifier = modifier
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentAlignment = Alignment.TopCenter
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        if (query.isBlank()) {
          // Empty State: Recent searches & Quick search suggestions
          EmptySearchDashboard(
            recentSearches = uiState.recentSearches,
            onSelectRecent = { q ->
              onQueryChanged(q)
              onExecuteSearch(q)
            },
            onClearRecent = onClearRecentSearches,
            habits = uiState.habits,
            tasks = uiState.generalTasks,
            goals = uiState.goals,
            onSelectGoal = onSelectGoal,
            onToggleGeneralTask = onToggleGeneralTask
          )
        } else if (searchResults.isEmpty()) {
          // No Results Found State
          NoResultsState(query = query)
        } else {
          // Search Results List
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "نتائج البحث (${searchResults.size})",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = TextPrimary
            )
            Text(
              text = uiState.activeSearchCategory.titleArabic,
              style = MaterialTheme.typography.labelSmall,
              color = BrightBlue
            )
          }

          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(searchResults, key = { it.category.name + "_" + it.id }) { item ->
              SearchResultCard(
                item = item,
                onSelectGoal = onSelectGoal,
                onSelectCalendarDate = onSelectCalendarDate,
                onToggleGeneralTask = onToggleGeneralTask,
                onToggleGoalTask = onToggleGoalTask,
                onPostponeTask = onPostponeTask,
                onPostponeGoalTask = onPostponeGoalTask,
                onIncrementHabit = onIncrementHabit,
                onToggleHabitBoolean = onToggleHabitBoolean
              )
            }
            item {
              Spacer(modifier = Modifier.height(24.dp))
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmptySearchDashboard(
  recentSearches: List<String>,
  onSelectRecent: (String) -> Unit,
  onClearRecent: () -> Unit,
  habits: List<com.example.model.Habit>,
  tasks: List<com.example.model.Task>,
  goals: List<Goal>,
  onSelectGoal: (String) -> Unit,
  onToggleGeneralTask: (String) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Recent Searches
    if (recentSearches.isNotEmpty()) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.History,
              contentDescription = "عمليات البحث الأخيرة",
              tint = TextSecondary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "عمليات البحث الأخيرة",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
              color = TextPrimary
            )
          }

          TextButton(onClick = onClearRecent) {
            Text(
              text = "مسح السجل",
              style = MaterialTheme.typography.labelSmall,
              color = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          recentSearches.forEach { search ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceWhite)
                .clickable { onSelectRecent(search) }
                .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
              Text(
                text = "🔍 $search",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = TextPrimary
              )
            }
          }
        }
      }
    }

    // Quick suggestions header
    Text(
      text = "اقتراحات سريعة للوصول ⚡",
      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
      color = TextPrimary
    )

    // Quick suggestions cards
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      QuickSuggestionPill(
        icon = "🕌",
        title = "الصلوات والورد",
        subtitle = "عادات أساسية",
        onClick = { onSelectRecent("الصلوات") },
        modifier = Modifier.weight(1f)
      )
      QuickSuggestionPill(
        icon = "🏋️‍♂️",
        title = "تمارين رياضية",
        subtitle = "جيم ولياقة",
        onClick = { onSelectRecent("جيم") },
        modifier = Modifier.weight(1f)
      )
      QuickSuggestionPill(
        icon = "🎯",
        title = "الأهداف النشطة",
        subtitle = "متابعة التقدم",
        onClick = { onSelectRecent("اللغة الإنجليزية") },
        modifier = Modifier.weight(1f)
      )
    }

    // Pro Tips Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("💡", fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "ميزة البحث الذكي الشامل",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = BrightBlue
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "يتعرف البحث تلقائياً على همزات وأحرف اللغة العربية ويبحث في المهام والعادات والأهداف والأفكار وسجلات التقويم بدقة متناهية.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
            color = TextSecondary
          )
        }
      }
    }
  }
}

@Composable
private fun QuickSuggestionPill(
  icon: String,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .background(SurfaceWhite)
      .clickable(onClick = onClick)
      .padding(12.dp)
  ) {
    Column {
      Text(icon, fontSize = 20.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = TextPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = TextSecondary
      )
    }
  }
}

@Composable
private fun NoResultsState(query: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 48.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(72.dp)
        .clip(CircleShape)
        .background(Color(0xFFEFF6FF)),
      contentAlignment = Alignment.Center
    ) {
      Text("🔍", fontSize = 32.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "لا توجد نتائج مطابقة لـ \"$query\"",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = TextPrimary
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "جرّب كتابة كلمة أقصر، أو اختر تصنيف \"الكل\" للبحث عبر كافة أقسام التطبيق.",
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )
  }
}

@Composable
private fun SearchResultCard(
  item: SearchResultItem,
  onSelectGoal: (String) -> Unit,
  onSelectCalendarDate: (String) -> Unit,
  onToggleGeneralTask: (String) -> Unit,
  onToggleGoalTask: (String, String) -> Unit,
  onPostponeTask: (String) -> Unit,
  onPostponeGoalTask: (String, String) -> Unit,
  onIncrementHabit: (String) -> Unit,
  onToggleHabitBoolean: (String) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    when (item) {
      is SearchResultItem.TaskItem -> {
        TaskSearchResultRow(
          taskItem = item,
          onToggle = {
            if (item.isGoalTask && item.goalId != null) {
              onToggleGoalTask(item.goalId, item.task.id)
            } else {
              onToggleGeneralTask(item.task.id)
            }
          },
          onPostpone = {
            if (item.isGoalTask && item.goalId != null) {
              onPostponeGoalTask(item.goalId, item.task.id)
            } else {
              onPostponeTask(item.task.id)
            }
          }
        )
      }
      is SearchResultItem.HabitItem -> {
        HabitSearchResultRow(
          habitItem = item,
          onIncrement = { onIncrementHabit(item.habit.id) },
          onToggleBoolean = { onToggleHabitBoolean(item.habit.id) }
        )
      }
      is SearchResultItem.GoalItem -> {
        GoalSearchResultRow(
          goalItem = item,
          onClick = { onSelectGoal(item.goal.id) }
        )
      }
      is SearchResultItem.NoteItem -> {
        NoteSearchResultRow(noteItem = item)
      }
      is SearchResultItem.CalendarItem -> {
        CalendarSearchResultRow(
          calendarItem = item,
          onClick = { onSelectCalendarDate(item.dateKey) }
        )
      }
    }
  }
}

@Composable
private fun TaskSearchResultRow(
  taskItem: SearchResultItem.TaskItem,
  onToggle: () -> Unit,
  onPostpone: () -> Unit
) {
  val task = taskItem.task
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(
      checked = task.isCompleted,
      onCheckedChange = { onToggle() },
      colors = CheckboxDefaults.colors(checkedColor = BrightBlue)
    )

    Spacer(modifier = Modifier.width(6.dp))

    Column(modifier = Modifier.weight(1f)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          ),
          color = if (task.isCompleted) TextMuted else TextPrimary,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }

      taskItem.subtitle?.let {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = it,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = TextSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Column(horizontalAlignment = Alignment.End) {
      PriorityBadge(priority = task.priority)
      if (!task.isCompleted && !task.isPostponed) {
        Spacer(modifier = Modifier.height(4.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE0F2FE))
            .clickable(onClick = onPostpone)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "للغد ➡️",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = BrightBlue
          )
        }
      }
    }
  }
}

@Composable
private fun HabitSearchResultRow(
  habitItem: SearchResultItem.HabitItem,
  onIncrement: () -> Unit,
  onToggleBoolean: () -> Unit
) {
  val habit = habitItem.habit
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Text(habit.iconEmoji, fontSize = 24.sp)
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = habit.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        Text(
          text = habitItem.subtitle,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = TextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(HabitEmerald)
        .clickable {
          if (habit.type == com.example.model.HabitType.BOOLEAN) onToggleBoolean() else onIncrement()
        }
        .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
      Text(
        text = if (habit.isCompleted) "مكتمل ✅" else "+1 تسجيل",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      )
    }
  }
}

@Composable
private fun GoalSearchResultRow(
  goalItem: SearchResultItem.GoalItem,
  onClick: () -> Unit
) {
  val goal = goalItem.goal
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(goal.iconEmoji, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = goal.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
          )
          goalItem.subtitle?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = TextSecondary
            )
          }
        }
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(GoalBlueBg)
          .padding(horizontal = 8.dp, vertical = 3.dp)
      ) {
        Text(
          text = "${goal.progressPercentage}%",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = BrightBlue
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    LinearProgressIndicator(
      progress = { goal.progressPercentage / 100f },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = BrightBlue,
      trackColor = Color(0xFFF1F5F9),
      strokeCap = StrokeCap.Round
    )
  }
}

@Composable
private fun NoteSearchResultRow(noteItem: SearchResultItem.NoteItem) {
  val note = noteItem.note
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(note.colorLong).copy(alpha = 0.4f))
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("💡", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = note.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
      }

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White.copy(alpha = 0.8f))
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Text(
          text = note.tag,
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = TextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = note.content,
      style = MaterialTheme.typography.bodySmall.copy(lineHeight = 15.sp),
      color = TextSecondary,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
private fun CalendarSearchResultRow(
  calendarItem: SearchResultItem.CalendarItem,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text("📅", fontSize = 22.sp)
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = calendarItem.title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = TextPrimary
        )
        calendarItem.subtitle.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextSecondary
          )
        }
      }
    }

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFECFDF5))
        .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
      Text(
        text = "عرض الأجندة 🔍",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = HabitEmerald
        )
      )
    }
  }
}
