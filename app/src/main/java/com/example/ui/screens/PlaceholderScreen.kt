package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
  destination: ScreenDestination,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onNavigateBack()
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = destination.titleArabic,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "الرجوع",
              tint = TextPrimary
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
      )
    },
    containerColor = BackgroundLight,
    modifier = modifier
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(GoalBlueBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = destination.icon,
          contentDescription = destination.titleArabic,
          tint = BrightBlue,
          modifier = Modifier.size(40.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "قسم ${destination.titleArabic}",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp
        ),
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "سيتم تفعيل هذا القسم بالكامل في المراحل القادمة ضمن نظام MyOS المتكامل.",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onNavigateBack,
        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("العودة إلى الرئيسية", fontWeight = FontWeight.Bold)
      }
    }
  }
}
