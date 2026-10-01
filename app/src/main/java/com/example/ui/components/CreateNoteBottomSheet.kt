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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Note
import com.example.model.NotePastelColors
import com.example.model.NoteTagProvider
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteBottomSheet(
  isVisible: Boolean,
  editingNote: Note? = null,
  onDismiss: () -> Unit,
  onSaveNote: (
    title: String,
    content: String,
    colorLong: Long,
    tag: String,
    isPinned: Boolean
  ) -> Unit
) {
  if (!isVisible) return

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var title by remember(editingNote) { mutableStateOf(editingNote?.title ?: "") }
  var content by remember(editingNote) { mutableStateOf(editingNote?.content ?: "") }
  var selectedColor by remember(editingNote) { mutableLongStateOf(editingNote?.colorLong ?: 0xFFFFFBEB) }
  var selectedTag by remember(editingNote) { mutableStateOf(editingNote?.tag ?: "خواطر وتأملات") }
  var isPinned by remember(editingNote) { mutableStateOf(editingNote?.isPinned ?: false) }

  var titleError by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(selectedColor),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .verticalScroll(rememberScrollState())
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (editingNote == null) "💡 تدوين فكرة جديدة" else "✏️ تعديل الفكرة",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = TextPrimary
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Pin button
          IconButton(
            onClick = { isPinned = !isPinned },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
              contentDescription = if (isPinned) "مثبتة" else "تثبيت",
              tint = if (isPinned) BrightBlue else TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Title Input
      OutlinedTextField(
        value = title,
        onValueChange = {
          title = it
          if (it.isNotBlank()) titleError = false
        },
        placeholder = { Text("عنوان الفكرة أو الخاطرة...", color = TextSecondary.copy(alpha = 0.7f)) },
        isError = titleError,
        supportingText = if (titleError) {
          { Text("يرجى كتابة عنوان للفكرة", color = MaterialTheme.colorScheme.error) }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceWhite.copy(alpha = 0.9f),
          unfocusedContainerColor = SurfaceWhite.copy(alpha = 0.7f),
          focusedBorderColor = BrightBlue,
          unfocusedBorderColor = BorderLight
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Content Input (Multi-line like Google Keep)
      OutlinedTextField(
        value = content,
        onValueChange = { content = it },
        placeholder = { Text("اكتب تفاصيل ما يدور في ذهنك بحرية... (يمكنك تحويلها لاحقاً لهدف أو عادة أو مهمة)", color = TextSecondary.copy(alpha = 0.7f)) },
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceWhite.copy(alpha = 0.9f),
          unfocusedContainerColor = SurfaceWhite.copy(alpha = 0.7f),
          focusedBorderColor = BrightBlue,
          unfocusedBorderColor = BorderLight
        ),
        maxLines = 8
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Category / Tag Selection
      Text(
        text = "تصنيف الفكرة في المخزن:",
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
        color = TextPrimary
      )
      Spacer(modifier = Modifier.height(6.dp))

      val tagsList = NoteTagProvider.defaultTags.filter { it.id != "all" }
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(tagsList) { tagItem ->
          val isSelected = selectedTag == tagItem.nameArabic
          FilterChip(
            selected = isSelected,
            onClick = { selectedTag = tagItem.nameArabic },
            label = {
              Text(
                text = "${tagItem.emoji} ${tagItem.nameArabic}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = tagItem.color,
              selectedLabelColor = TextWhite,
              containerColor = SurfaceWhite.copy(alpha = 0.8f),
              labelColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            border = null
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Color Palette Picker (Google Keep Style)
      Text(
        text = "لون الملاحظة:",
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
        color = TextPrimary
      )
      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        NotePastelColors.forEach { noteColor ->
          val isColorSelected = selectedColor == noteColor.colorLong
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(noteColor.colorLong))
              .border(
                width = if (isColorSelected) 2.5.dp else 1.dp,
                color = if (isColorSelected) BrightBlue else BorderLight,
                shape = CircleShape
              )
              .clickable { selectedColor = noteColor.colorLong },
            contentAlignment = Alignment.Center
          ) {
            if (isColorSelected) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "محدد",
                tint = BrightBlue,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Save Button
      Button(
        onClick = {
          if (title.isBlank()) {
            titleError = true
          } else {
            onSaveNote(
              title.trim(),
              content.trim(),
              selectedColor,
              selectedTag,
              isPinned
            )
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
      ) {
        Text(
          text = if (editingNote == null) "حفظ في مخزن الأفكار 💡" else "حفظ التعديلات ✅",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          ),
          color = TextWhite
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
