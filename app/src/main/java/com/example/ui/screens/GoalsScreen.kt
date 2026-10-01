package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Goal
import com.example.model.Priority
import com.example.model.ScreenDestination
import com.example.ui.components.CreateGoalBottomSheet
import com.example.ui.components.GoalOverviewCard
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.MyOSHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.viewmodel.GoalFilter
import com.example.viewmodel.MyOSUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onToggleRestMode: () -> Unit,
  onSelectGoal: (String) -> Unit,
  onSetGoalFilter: (GoalFilter) -> Unit,
  onOpenCreateGoal: () -> Unit,
  onCloseCreateGoal: () -> Unit,
  onSaveGoal: (title: String, description: String?, iconId: String, priority: Priority, dueDate: String?) -> Unit,
  onDeleteGoal: (String) -> Unit,
  onDismissNotification: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onScreenSelected(ScreenDestination.HOME)
  }

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
      ModalDrawerSheet(drawerContainerColor = SurfaceWhite) {
        NavigationDrawerContent(
          currentScreen = ScreenDestination.GOALS,
          onScreenSelected = onScreenSelected,
          onCloseDrawer = { scope.launch { drawerState.close() } }
        )
      }
    }
  ) {
    Scaffold(
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
          currentScreen = ScreenDestination.GOALS,
          onTabSelected = onScreenSelected,
          onMoreClick = { scope.launch { drawerState.open() } }
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = onOpenCreateGoal,
          shape = CircleShape,
          containerColor = Color.Transparent,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
          modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape)
            .background(
              brush = Brush.linearGradient(
                colors = listOf(
                  BrightBlue,
                  DeepBlue,
                  ElectricViolet
                )
              ),
              shape = CircleShape
            )
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "إضافة هدف جديد",
            tint = TextWhite,
            modifier = Modifier.size(28.dp)
          )
        }
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 1. Title & Subtitle Section
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Outlined.TrackChanges,
                contentDescription = null,
                tint = BrightBlue,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "الأهداف",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp
                ),
                color = TextPrimary
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "أحلام كبيرة .. تخطيط ذكي .. نتائج حقيقية",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              ),
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 2. Quick Action Card ("إضافة هدف جديد")
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .border(1.dp, BrightBlue.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
              .clickable(onClick = onOpenCreateGoal),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Right: Button icon + texts
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(GoalBlueBg),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = BrightBlue,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = "إضافة هدف جديد",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp
                    ),
                    color = BrightBlue
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "ابدأ رحلتك نحو تحقيق أهدافك",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondary
                  )
                }
              }

              // Left: Arrow
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = BrightBlue.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3. Filter Chips Row ("الكل", "النشطة", "المتوقفة مؤقتاً", "المكتملة")
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            GoalFilter.entries.forEach { filter ->
              val isSelected = uiState.activeGoalFilter == filter
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) BrightBlue else SurfaceWhite)
                  .border(
                    width = 1.dp,
                    color = if (isSelected) BrightBlue else BorderLight,
                    shape = RoundedCornerShape(12.dp)
                  )
                  .clickable { onSetGoalFilter(filter) }
                  .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = filter.titleArabic,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                  ),
                  color = if (isSelected) TextWhite else TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 4. Goals List
          val filteredGoals = uiState.filteredGoals
          if (filteredGoals.isEmpty()) {
            // Empty State
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BorderLight.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 36.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(GoalBlueBg),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Outlined.TrackChanges,
                    contentDescription = null,
                    tint = BrightBlue,
                    modifier = Modifier.size(32.dp)
                  )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = "لسه مفيش أهداف",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                  ),
                  color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = "ابدأ بهدف صغير، وابني عليه خطوة بخطوة.",
                  style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                  color = TextSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                  onClick = onOpenCreateGoal,
                  colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("إضافة هدف جديد", fontWeight = FontWeight.Bold)
                }
              }
            }
          } else {
            filteredGoals.forEach { goal ->
              GoalOverviewCard(
                goal = goal,
                onClick = { onSelectGoal(goal.id) }
              )
              Spacer(modifier = Modifier.height(10.dp))
            }
          }

          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }

  // Create Goal Bottom Sheet
  if (uiState.isCreateGoalSheetVisible) {
    CreateGoalBottomSheet(
      isVisible = true,
      editingGoal = uiState.editingGoal,
      onDismiss = onCloseCreateGoal,
      onSaveGoal = onSaveGoal,
      onDeleteGoal = onDeleteGoal
    )
  }
}
