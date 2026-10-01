package com.example.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Note
import com.example.model.NoteTagProvider
import com.example.model.ScreenDestination
import com.example.ui.components.CreateNoteBottomSheet
import com.example.ui.components.MyOSBottomNavigationBar
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
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
fun NotesScreen(
  uiState: MyOSUiState,
  onScreenSelected: (ScreenDestination) -> Unit,
  onOpenCreateNote: () -> Unit,
  onOpenEditNote: (Note) -> Unit,
  onCloseCreateNote: () -> Unit,
  onSaveNote: (title: String, content: String, colorLong: Long, tag: String, isPinned: Boolean) -> Unit,
  onTogglePinNote: (String) -> Unit,
  onDeleteNote: (String) -> Unit,
  onSetActiveTag: (String) -> Unit,
  onSetSearchQuery: (String) -> Unit,
  onConvertToGoal: (Note) -> Unit,
  onConvertToHabit: (Note) -> Unit,
  onConvertToTask: (Note) -> Unit,
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

  // Quick capture field state at top
  var quickTitle by remember { mutableStateOf("") }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(drawerContainerColor = SurfaceWhite) {
        NavigationDrawerContent(
          currentScreen = ScreenDestination.NOTES,
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
              .padding(top = 8.dp, bottom = 8.dp)
          ) {
            // Top Bar Row: Hamburger, Title, Count
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "💡 مخزن الأفكار",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 17.sp
                    ),
                    color = TextPrimary
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(Color(0xFFFEF3C7))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "Inbox",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      ),
                      color = Color(0xFFB45309)
                    )
                  }
                }
              }

              Text(
                text = "${uiState.filteredNotes.size} فكرة محفوظة",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Medium,
                  fontSize = 11.sp
                ),
                color = TextSecondary
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
              value = uiState.noteSearchQuery,
              onValueChange = onSetSearchQuery,
              placeholder = { Text("ابحث في الأفكار والخواطر...", fontSize = 13.sp, color = TextSecondary) },
              leadingIcon = {
                Icon(
                  imageVector = Icons.Outlined.Search,
                  contentDescription = "بحث",
                  tint = TextSecondary,
                  modifier = Modifier.size(18.dp)
                )
              },
              trailingIcon = {
                if (uiState.noteSearchQuery.isNotEmpty()) {
                  IconButton(
                    onClick = { onSetSearchQuery("") },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "مسح",
                      tint = TextSecondary,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .height(48.dp),
              shape = RoundedCornerShape(24.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8FAFC),
                unfocusedContainerColor = Color(0xFFF8FAFC),
                focusedBorderColor = BrightBlue,
                unfocusedBorderColor = BorderLight
              ),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tags / Categories Filter Row
            LazyRow(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              items(NoteTagProvider.defaultTags) { tagItem ->
                val isSelected = uiState.activeNoteTag == tagItem.nameArabic
                FilterChip(
                  selected = isSelected,
                  onClick = { onSetActiveTag(tagItem.nameArabic) },
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
                    selectedContainerColor = BrightBlue,
                    selectedLabelColor = TextWhite,
                    containerColor = Color(0xFFF1F5F9),
                    labelColor = TextPrimary
                  ),
                  shape = RoundedCornerShape(16.dp),
                  border = null,
                  modifier = Modifier.height(30.dp)
                )
              }
            }
          }
        }
      },
      bottomBar = {
        MyOSBottomNavigationBar(
          currentScreen = ScreenDestination.NOTES,
          onTabSelected = onScreenSelected,
          onMoreClick = { scope.launch { drawerState.open() } }
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = onOpenCreateNote,
          shape = CircleShape,
          containerColor = Color.Transparent,
          elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
          modifier = Modifier
            .size(56.dp)
            .shadow(6.dp, CircleShape)
            .background(
              brush = Brush.linearGradient(
                colors = listOf(BrightBlue, DeepBlue, ElectricViolet)
              ),
              shape = CircleShape
            )
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "تدوين فكرة",
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
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Spacer(modifier = Modifier.height(10.dp))

          // Quick Capture Box at Top (Google Keep Style Quick Input)
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp)
              .clip(RoundedCornerShape(18.dp))
              .border(1.dp, BorderLight.copy(alpha = 0.8f), RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              OutlinedTextField(
                value = quickTitle,
                onValueChange = { quickTitle = it },
                placeholder = {
                  Text("دوّن فكرة سريعة قبل أن تنساها...", fontSize = 13.sp, color = TextSecondary)
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = Color.Transparent,
                  unfocusedContainerColor = Color.Transparent,
                  focusedBorderColor = Color.Transparent,
                  unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
              )
              if (quickTitle.isNotBlank()) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(BrightBlue)
                    .clickable {
                      onSaveNote(
                        quickTitle.trim(),
                        "",
                        0xFFFFFBEB,
                        "خواطر وتأملات",
                        false
                      )
                      quickTitle = ""
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = "حفظ",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    ),
                    color = TextWhite
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Notes List (Google Keep Cards)
          val filteredNotes = uiState.filteredNotes
          if (filteredNotes.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💡", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = "لا توجد أفكار في هذا التصنيف حالياً",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                  ),
                  color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "اضغط على زر الإضافة لتدوين أي فكرة أو مشروع أو عادة تخطر ببالك!",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                  color = TextSecondary
                )
              }
            }
          } else {
            filteredNotes.forEach { note ->
              IdeaNoteCard(
                note = note,
                onEdit = { onOpenEditNote(note) },
                onTogglePin = { onTogglePinNote(note.id) },
                onDelete = { onDeleteNote(note.id) },
                onConvertToGoal = { onConvertToGoal(note) },
                onConvertToHabit = { onConvertToHabit(note) },
                onConvertToTask = { onConvertToTask(note) }
              )
              Spacer(modifier = Modifier.height(10.dp))
            }
          }

          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }

  // Create / Edit Note Sheet
  CreateNoteBottomSheet(
    isVisible = uiState.isCreateNoteSheetVisible,
    editingNote = uiState.editingNote,
    onDismiss = onCloseCreateNote,
    onSaveNote = onSaveNote
  )
}

// ---------------------------------------------------------
// Idea Note Card (Google Keep inspired)
// ---------------------------------------------------------
@Composable
private fun IdeaNoteCard(
  note: Note,
  onEdit: () -> Unit,
  onTogglePin: () -> Unit,
  onDelete: () -> Unit,
  onConvertToGoal: () -> Unit,
  onConvertToHabit: () -> Unit,
  onConvertToTask: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, BorderLight.copy(alpha = 0.7f), RoundedCornerShape(18.dp)),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color(note.colorLong)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Header: Tag, Pin & Delete
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Tag badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceWhite.copy(alpha = 0.75f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = note.tag,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Pin button
          IconButton(
            onClick = onTogglePin,
            modifier = Modifier.size(30.dp)
          ) {
            Icon(
              imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
              contentDescription = if (note.isPinned) "مثبتة" else "تثبيت",
              tint = if (note.isPinned) BrightBlue else TextSecondary.copy(alpha = 0.7f),
              modifier = Modifier.size(17.dp)
            )
          }

          // Edit button
          IconButton(
            onClick = onEdit,
            modifier = Modifier.size(30.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "تعديل",
              tint = TextSecondary.copy(alpha = 0.7f),
              modifier = Modifier.size(16.dp)
            )
          }

          // Delete button
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(30.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = "حذف",
              tint = Color(0xFFEF4444).copy(alpha = 0.7f),
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Note Title
      Text(
        text = note.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        ),
        color = TextPrimary,
        modifier = Modifier.clickable(onClick = onEdit)
      )

      // Note Content
      if (note.content.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = note.content,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 12.sp,
            lineHeight = 18.sp
          ),
          color = TextSecondary,
          modifier = Modifier.clickable(onClick = onEdit)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Footer: Date and Transformation Actions (تحويل الفكرة إلى هدف / عادة / مهمة)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = note.formattedDate,
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
          color = TextMuted
        )

        // Action Buttons: Convert idea to Goal, Habit, Task
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          // Convert to Goal
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(GoalBlueBg)
              .clickable(onClick = onConvertToGoal)
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Text(
              text = "🎯 لهدف",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              ),
              color = GoalBlue
            )
          }

          // Convert to Habit
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(HabitEmeraldBg)
              .clickable(onClick = onConvertToHabit)
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Text(
              text = "🌱 لعادة",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              ),
              color = HabitEmerald
            )
          }

          // Convert to Task
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(TaskVioletBg)
              .clickable(onClick = onConvertToTask)
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Text(
              text = "⚡ لمهمة",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              ),
              color = TaskViolet
            )
          }
        }
      }
    }
  }
}
