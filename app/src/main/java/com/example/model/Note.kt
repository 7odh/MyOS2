package com.example.model

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NoteTag(
  val id: String,
  val nameArabic: String,
  val emoji: String,
  val color: Color
)

object NoteTagProvider {
  val defaultTags = listOf(
    NoteTag("all", "الكل", "💡", Color(0xFF3B82F6)),
    NoteTag("goals", "أفكار أهداف", "🎯", Color(0xFF2563EB)),
    NoteTag("habits", "عادات مقترحة", "🌱", Color(0xFF10B981)),
    NoteTag("projects", "مشاريع مستقبلية", "🚀", Color(0xFF8B5CF6)),
    NoteTag("thoughts", "خواطر وتأملات", "💭", Color(0xFFF59E0B)),
    NoteTag("resources", "كتب ومصادر", "📚", Color(0xFFEC4899))
  )
}

data class NoteColor(
  val colorLong: Long,
  val name: String
)

val NotePastelColors = listOf(
  NoteColor(0xFFFFFBEB, "أصفر دافئ"),
  NoteColor(0xFFEFF6FF, "أزرق سماوي"),
  NoteColor(0xFFECFDF5, "نعناعي زمردي"),
  NoteColor(0xFFFAF5FF, "بنفسجي هادئ"),
  NoteColor(0xFFFFF1F2, "وردي ناعم"),
  NoteColor(0xFFFFF7ED, "خوخي مشرق"),
  NoteColor(0xFFF8FAFC, "رمادي ناصع")
)

data class Note(
  val id: String,
  val title: String,
  val content: String,
  val colorLong: Long = 0xFFFFFBEB,
  val isPinned: Boolean = false,
  val tag: String = "أفكار عامة",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
) {
  val formattedDate: String
    get() {
      val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.ENGLISH)
      return sdf.format(Date(updatedAt))
    }
}
