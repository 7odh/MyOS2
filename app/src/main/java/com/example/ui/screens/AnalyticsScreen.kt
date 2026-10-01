package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.AnalyticsTimePeriod
import com.example.model.ScreenDestination
import com.example.ui.components.AnalyticsActivityBarChart
import com.example.ui.components.AnalyticsFourPillarsSection
import com.example.ui.components.AnalyticsHabitsLeaderboardCard
import com.example.ui.components.AnalyticsOverviewCard
import com.example.ui.components.AnalyticsSmartInsightsSection
import com.example.ui.components.AnalyticsTimePeriodSelector
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.MyOSHeader
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.SurfaceWhite
import com.example.viewmodel.MyOSUiState
import kotlinx.coroutines.launch

@Composable
fun AnalyticsScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onToggleRestMode: () -> Unit,
  onSelectPeriod: (AnalyticsTimePeriod) -> Unit,
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
          currentScreen = uiState.currentScreen,
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
          currentScreen = uiState.currentScreen,
          onTabSelected = onScreenSelected
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
            .widthIn(max = 680.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // 1. Time Period Selector (اليوم، أسبوع، شهر، سنة)
          AnalyticsTimePeriodSelector(
            selectedPeriod = uiState.selectedAnalyticsPeriod,
            onSelectPeriod = onSelectPeriod
          )

          // 2. Hero Overview & Score Gauge Card
          AnalyticsOverviewCard(
            report = uiState.analyticsReport
          )

          // 3. Interactive Activity Bar Chart (Canvas)
          AnalyticsActivityBarChart(
            bars = uiState.analyticsReport.chartBars,
            periodTitle = uiState.analyticsReport.periodDisplayTitle
          )

          // 4. The 4 Core Pillars: التقدم، التقصير، الراحة، التأجيل
          AnalyticsFourPillarsSection(
            report = uiState.analyticsReport
          )

          // 5. Habit Consistency & Streaks Leaderboard
          AnalyticsHabitsLeaderboardCard(
            habits = uiState.analyticsReport.habitsLeaderboard
          )

          // 6. Smart AI/Coaching Insights
          AnalyticsSmartInsightsSection(
            insights = uiState.analyticsReport.insights
          )

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
