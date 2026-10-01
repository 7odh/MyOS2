package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CustomDurationDialog(
  initialMinutes: Int,
  onConfirm: (Int) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedMinutes by remember { mutableIntStateOf(initialMinutes.coerceIn(5, 120)) }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier,
    containerColor = SurfaceWhite,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(text = "⏱️", fontSize = 22.sp)
        Text(
          text = "تحديد مدة الجلسة",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "اختر مدة جلسة التركيز بالدقائق",
          fontSize = 13.sp,
          color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Large Number Display
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          IconButton(
            onClick = {
              if (selectedMinutes > 5) selectedMinutes -= 5
            },
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(GoalBlueBg)
          ) {
            Icon(
              imageVector = Icons.Default.Remove,
              contentDescription = "إنقاص",
              tint = BrightBlue
            )
          }

          Spacer(modifier = Modifier.width(20.dp))

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "$selectedMinutes",
              fontSize = 48.sp,
              fontWeight = FontWeight.Black,
              color = BrightBlue
            )
            Text(
              text = "دقيقة",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.width(20.dp))

          IconButton(
            onClick = {
              if (selectedMinutes < 180) selectedMinutes += 5
            },
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(GoalBlueBg)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "زيادة",
              tint = BrightBlue
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slider
        Slider(
          value = selectedMinutes.toFloat(),
          onValueChange = { selectedMinutes = (it / 5).toInt() * 5 },
          valueRange = 5f..180f,
          steps = 34,
          colors = SliderDefaults.colors(
            thumbColor = BrightBlue,
            activeTrackColor = BrightBlue,
            inactiveTrackColor = BorderLight
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Preset Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          listOf(15, 25, 45, 60, 90).forEach { mins ->
            val isCurrent = selectedMinutes == mins
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isCurrent) BrightBlue else GoalBlueBg)
                .clickable { selectedMinutes = mins }
                .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
              Text(
                text = "${mins}د",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) Color.White else BrightBlue
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(selectedMinutes) },
        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("تأكيد المدة ⏱️", color = Color.White, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("إلغاء", color = TextSecondary)
      }
    }
  )
}
