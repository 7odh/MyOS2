package com.example.data

import com.example.model.DailyAnalytics
import com.example.model.DayHabitRecord
import com.example.model.DayOfWeekArabic
import com.example.model.DaySummaryHistory
import com.example.model.DayTaskRecord
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Note
import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.model.User
import com.example.model.getCurrentDayOfWeekArabic
import com.example.model.getTodayDateString
import com.example.model.getTomorrowDateString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class MyOSRepository {

  private val _user = MutableStateFlow(
    User(
      name = "أحمد",
      motivationalSentence = "النجاح هو مجموع قرارات وانضباطات صغيرة.",
      isMotivationEnabled = true,
      isRestModeActive = false
    )
  )
  val user: StateFlow<User> = _user.asStateFlow()

  private val _habits = MutableStateFlow(
    listOf(
      Habit(
        id = "h1",
        title = "الصلوات الخمس",
        type = HabitType.COUNTER,
        targetValue = 5,
        currentValue = 3,
        unit = "صلوات",
        frequency = HabitFrequency.DAILY,
        isMandatory = true, // إجبارية لا تتأثر بوضع الراحة
        iconEmoji = "🕌",
        priority = Priority.HIGH,
        currentStreak = 24
      ),
      Habit(
        id = "h2",
        title = "الذهاب للجيم (تمارين رياضية)",
        type = HabitType.DURATION,
        targetValue = 60,
        currentValue = 0,
        unit = "دقيقة",
        frequency = HabitFrequency.SPECIFIC_DAYS,
        scheduledDays = listOf(DayOfWeekArabic.SATURDAY, DayOfWeekArabic.MONDAY, DayOfWeekArabic.THURSDAY),
        isMandatory = false, // تتأثر بوضع الراحة
        iconEmoji = "🏋️‍♂️",
        priority = Priority.HIGH,
        currentStreak = 8
      ),
      Habit(
        id = "h3",
        title = "الورد القرآني",
        type = HabitType.QUANTITY,
        targetValue = 10,
        currentValue = 6,
        unit = "صفحة",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "📖",
        priority = Priority.HIGH,
        currentStreak = 15
      ),
      Habit(
        id = "h4",
        title = "التدرب على الكيبورد",
        type = HabitType.DURATION,
        targetValue = 30,
        currentValue = 20,
        unit = "دقيقة",
        frequency = HabitFrequency.DAILY,
        isMandatory = false,
        iconEmoji = "⌨️",
        priority = Priority.MEDIUM,
        currentStreak = 9
      ),
      Habit(
        id = "h5",
        title = "تمارين الضغط",
        type = HabitType.QUANTITY,
        targetValue = 50,
        currentValue = 30,
        unit = "عدة",
        frequency = HabitFrequency.DAILY,
        isMandatory = false,
        iconEmoji = "💪",
        priority = Priority.MEDIUM,
        currentStreak = 11
      ),
      Habit(
        id = "h6",
        title = "شرب الماء",
        type = HabitType.COUNTER,
        targetValue = 8,
        currentValue = 5,
        unit = "أكواب",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "💧",
        priority = Priority.HIGH,
        currentStreak = 18
      ),
      Habit(
        id = "h7",
        title = "أذكار الصباح والمساء",
        type = HabitType.BOOLEAN,
        targetValue = 1,
        currentValue = 1,
        unit = "مرة",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "✨",
        priority = Priority.HIGH,
        currentStreak = 25
      )
    )
  )
  val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

  private val _generalTasks = MutableStateFlow(
    listOf(
      Task(
        id = "t1",
        title = "إنهاء تصميم الصفحة الرئيسية - MyOS",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t2",
        title = "مراجعة الكود الخاص بالمشروع",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t3",
        title = "تجهيز تقرير العمل",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t4",
        title = "إرسال التحديثات للفريق",
        priority = Priority.MEDIUM,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t5",
        title = "مراجعة البريد الإلكتروني الهام",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t6",
        title = "تنظيم ملفات الأسبوع",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t7",
        title = "إعداد خطة الأسبوع القادم",
        priority = Priority.MEDIUM,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t8",
        title = "تحديث ملاحظات الاجتماع",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t9",
        title = "نسخ احتياطي للبيانات",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t10",
        title = "تحديد أولويات الغد",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t11",
        title = "تجديد اشتراك خدمة التخزين السحابي",
        priority = Priority.LOW,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.NO_DATE
      ),
      Task(
        id = "t12",
        title = "تنظيف وترتيب المكتبة المنزلية",
        priority = Priority.LOW,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.NO_DATE
      ),
      Task(
        id = "t13",
        title = "شراء هدايا للمناسبة العائلية",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TOMORROW
      )
    )
  )
  val generalTasks: StateFlow<List<Task>> = _generalTasks.asStateFlow()

  private val _goals = MutableStateFlow(
    listOf(
      Goal(
        id = "g1",
        title = "إتقان اللغة الإنجليزية",
        description = "تعلم اللغة الإنجليزية وفتح آفاق جديدة للمستقبل",
        category = "مهارات ولغات",
        iconId = "book",
        iconEmoji = "📚",
        priority = Priority.MEDIUM,
        startDate = "2025/09/20",
        dueDate = "2025/12/31",
        tasks = listOf(
          Task(
            id = "gt1_1",
            title = "مراجعة القواعد الأساسية",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/22",
            createdAt = 1000L
          ),
          Task(
            id = "gt1_2",
            title = "حفظ 20 كلمة جديدة",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/25",
            createdAt = 2000L
          ),
          Task(
            id = "gt1_3",
            title = "ممارسة الاستماع",
            priority = Priority.LOW,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.NO_DATE,
            dueDateFormatted = null,
            createdAt = 3000L
          ),
          Task(
            id = "gt1_4",
            title = "ممارسة التحدث",
            priority = Priority.LOW,
            isCompleted = false,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.NO_DATE,
            dueDateFormatted = null,
            createdAt = 4000L
          )
        )
      ),
      Goal(
        id = "g2",
        title = "تطوير المهارات البرمجية",
        description = "تعلم تقنيات تطوير التطبيقات الحديثة وبناء مشاريع حقيقية",
        category = "مشاريع وتطوير",
        iconId = "code",
        iconEmoji = "</>",
        priority = Priority.HIGH,
        startDate = "2025/09/15",
        dueDate = "2025/11/30",
        tasks = listOf(
          Task(
            id = "gt2_1",
            title = "تصميم واجهة لوحة التحكم",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 100L
          ),
          Task(
            id = "gt2_2",
            title = "بناء نظام الأهداف وتفاصيلها",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 200L
          ),
          Task(
            id = "gt2_3",
            title = "إجراء اختبارات الأداء",
            priority = Priority.MEDIUM,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/05",
            createdAt = 300L
          ),
          Task(
            id = "gt2_4",
            title = "توثيق واجهات API",
            priority = Priority.LOW,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/10",
            createdAt = 400L
          ),
          Task(
            id = "gt2_5",
            title = "نشر النسخة التجريبية",
            priority = Priority.HIGH,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/20",
            createdAt = 500L
          )
        )
      ),
      Goal(
        id = "g3",
        title = "تحسين اللياقة البدنية",
        description = "الالتزام بنمط حياة صحي وممارسة الرياضة يومياً",
        category = "صحة ورياضة",
        iconId = "fitness",
        iconEmoji = "🏋️",
        priority = Priority.HIGH,
        startDate = "2025/09/01",
        dueDate = "2025/10/01",
        tasks = listOf(
          Task(
            id = "gt3_1",
            title = "تمارين الإحماء اليومية",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 10L
          ),
          Task(
            id = "gt3_2",
            title = "تمرين الجري 30 دقيقة",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 20L
          ),
          Task(
            id = "gt3_3",
            title = "شرب 3 لتر ماء",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 30L
          ),
          Task(
            id = "gt3_4",
            title = "تمارين القوة المنزلية",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/28",
            createdAt = 40L
          ),
          Task(
            id = "gt3_5",
            title = "جلسة استطالة ومساج",
            priority = Priority.LOW,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/28",
            createdAt = 50L
          )
        )
      )
    )
  )
  val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

  private val _notes = MutableStateFlow(
    listOf(
      Note(
        id = "n1",
        title = "فكرة تطبيق مساعد بالذكاء الاصطناعي",
        content = "تطبيق يساعد الطلاب على تلخيص المحاضرات الطويلة واستخراج الكلمات المفتاحية واختبار أنفسهم تلقائياً.",
        tag = "مشاريع مستقبلية",
        colorLong = 0xFFEFF6FF,
        isPinned = true
      ),
      Note(
        id = "n2",
        title = "عادة المشي 20 دقيقة بعد الفجر",
        content = "المشي في الهواء النقي بعد صلاة الفجر يساعد على تصفية الذهن وزيادة النشاط لليوم كاملاً.",
        tag = "عادات مقترحة",
        colorLong = 0xFFECFDF5,
        isPinned = true
      ),
      Note(
        id = "n3",
        title = "تعلم المحادثة باللغة الإنجليزية",
        content = "هدف للربع القادم: الانضمام لمجموعات محادثة أسبوعية والتركيز على الطلاقة بدون خوف من الخطأ.",
        tag = "أفكار أهداف",
        colorLong = 0xFFFAF5FF,
        isPinned = false
      ),
      Note(
        id = "n4",
        title = "الانضباط التراكمي وتأثير الـ 1%",
        content = "\"أنت لا ترتقي إلى مستوى أهدافك، بل تهبط إلى مستوى أنظمتك.\" التحسين الصغير اليومي يصنع فارقاً هائلاً عبر السنين.",
        tag = "خواطر وتأملات",
        colorLong = 0xFFFFFBEB,
        isPinned = false
      ),
      Note(
        id = "n5",
        title = "قائمة كتب مقترحة للشهر القادم",
        content = "1. العادات الذرية\n2. التركيز الفائق (Deep Work)\n3. التفكير السريع والبطيء\n4. أسبوع عمل من 4 ساعات",
        tag = "كتب ومصادر",
        colorLong = 0xFFFFF1F2,
        isPinned = false
      )
    )
  )
  val notes: StateFlow<List<Note>> = _notes.asStateFlow()

  fun updateUserName(newName: String) {
    _user.update { it.copy(name = newName.trim().ifEmpty { it.name }) }
  }

  fun toggleRestMode() {
    _user.update { it.copy(isRestModeActive = !it.isRestModeActive) }
  }

  fun toggleMotivation(enabled: Boolean) {
    _user.update { it.copy(isMotivationEnabled = enabled) }
  }

  fun updateMotivationalSentence(sentence: String) {
    _user.update { it.copy(motivationalSentence = sentence) }
  }

  fun updateUser(updatedUser: User) {
    _user.value = updatedUser
  }

  fun incrementHabit(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(currentValue = habit.currentValue + 1)
        } else habit
      }
    }
  }

  fun updateHabitProgress(habitId: String, newTotalValue: Int) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(currentValue = newTotalValue.coerceAtLeast(0))
        } else habit
      }
    }
  }

  fun toggleHabitBoolean(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          val nextVal = if (habit.currentValue >= habit.targetValue) 0 else habit.targetValue
          habit.copy(currentValue = nextVal)
        } else habit
      }
    }
  }

  fun toggleHabit(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          when (habit.type) {
            HabitType.COUNTER -> habit.copy(currentValue = habit.currentValue + 1)
            HabitType.BOOLEAN -> {
              val nextVal = if (habit.currentValue >= habit.targetValue) 0 else habit.targetValue
              habit.copy(currentValue = nextVal)
            }
            HabitType.QUANTITY, HabitType.DURATION -> {
              habit.copy(currentValue = habit.currentValue + 1)
            }
          }
        } else habit
      }
    }
  }

  fun toggleGeneralTask(taskId: String) {
    _generalTasks.update { list ->
      list.map { task ->
        if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
      }
    }
  }

  fun toggleGoalTask(goalId: String, taskId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          val updatedTasks = goal.tasks.map { task ->
            if (task.id == taskId) {
              val newCompleted = !task.isCompleted
              task.copy(
                isCompleted = newCompleted,
                completedAt = if (newCompleted) System.currentTimeMillis() else null
              )
            } else {
              task
            }
          }
          goal.copy(tasks = updatedTasks)
        } else {
          goal
        }
      }
    }
  }

  fun toggleGoalPause(goalId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) goal.copy(isPaused = !goal.isPaused) else goal
      }
    }
  }

  fun addGoal(
    title: String,
    description: String? = null,
    iconId: String = "target",
    priority: Priority = Priority.HIGH,
    dueDate: String? = null,
    category: String? = null
  ) {
    val newGoal = Goal(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description?.trim()?.ifEmpty { null },
      category = category ?: "أهداف شخصية",
      iconId = iconId,
      priority = priority,
      dueDate = dueDate?.trim()?.ifEmpty { null },
      startDate = "2025/09/29",
      tasks = emptyList()
    )
    _goals.update { listOf(newGoal) + it }
  }

  fun updateGoal(
    goalId: String,
    title: String,
    description: String?,
    iconId: String,
    priority: Priority,
    dueDate: String?
  ) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(
            title = title,
            description = description?.trim()?.ifEmpty { null },
            iconId = iconId,
            priority = priority,
            dueDate = dueDate?.trim()?.ifEmpty { null }
          )
        } else {
          goal
        }
      }
    }
  }

  fun deleteGoal(goalId: String) {
    _goals.update { list -> list.filterNot { it.id == goalId } }
  }

  fun addGoalTask(
    goalId: String,
    title: String,
    notes: String? = null,
    priority: Priority = Priority.MEDIUM,
    schedule: TaskSchedule = TaskSchedule.TODAY,
    dueDateFormatted: String? = null
  ) {
    val newTask = Task(
      id = UUID.randomUUID().toString(),
      title = title,
      notes = notes?.trim()?.ifEmpty { null },
      priority = priority,
      isCompleted = false,
      goalId = goalId,
      isGoalTask = true,
      schedule = schedule,
      dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null },
      createdAt = System.currentTimeMillis()
    )
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(tasks = goal.tasks + newTask)
        } else {
          goal
        }
      }
    }
  }

  fun updateGoalTask(
    goalId: String,
    taskId: String,
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          val updatedTasks = goal.tasks.map { task ->
            if (task.id == taskId) {
              task.copy(
                title = title,
                notes = notes?.trim()?.ifEmpty { null },
                priority = priority,
                schedule = schedule,
                dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null }
              )
            } else {
              task
            }
          }
          goal.copy(tasks = updatedTasks)
        } else {
          goal
        }
      }
    }
  }

  fun deleteGoalTask(goalId: String, taskId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(tasks = goal.tasks.filterNot { it.id == taskId })
        } else {
          goal
        }
      }
    }
  }

  fun addHabit(
    title: String,
    type: HabitType = HabitType.BOOLEAN,
    targetValue: Int = 1,
    unit: String = "مرة",
    frequency: HabitFrequency = HabitFrequency.DAILY,
    scheduledDays: List<DayOfWeekArabic> = emptyList(),
    isMandatory: Boolean = false,
    priority: Priority = Priority.HIGH,
    iconEmoji: String = "🌱"
  ) {
    val newHabit = Habit(
      id = UUID.randomUUID().toString(),
      title = title.trim(),
      type = type,
      targetValue = targetValue.coerceAtLeast(1),
      currentValue = 0,
      unit = unit.trim().ifEmpty { "مرة" },
      frequency = frequency,
      scheduledDays = if (frequency == HabitFrequency.SPECIFIC_DAYS) scheduledDays else emptyList(),
      isMandatory = isMandatory,
      priority = priority,
      currentStreak = 0,
      iconEmoji = iconEmoji
    )
    _habits.update { listOf(newHabit) + it }
  }

  fun updateHabit(
    habitId: String,
    title: String,
    type: HabitType,
    targetValue: Int,
    unit: String,
    frequency: HabitFrequency,
    scheduledDays: List<DayOfWeekArabic>,
    isMandatory: Boolean,
    priority: Priority,
    iconEmoji: String
  ) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(
            title = title.trim(),
            type = type,
            targetValue = targetValue.coerceAtLeast(1),
            unit = unit.trim().ifEmpty { "مرة" },
            frequency = frequency,
            scheduledDays = if (frequency == HabitFrequency.SPECIFIC_DAYS) scheduledDays else emptyList(),
            isMandatory = isMandatory,
            priority = priority,
            iconEmoji = iconEmoji
          )
        } else habit
      }
    }
  }

  fun deleteHabit(habitId: String) {
    _habits.update { list -> list.filterNot { it.id == habitId } }
  }

  fun pauseHabit(habitId: String, durationDays: Int?) {
    val pauseUntil = if (durationDays != null && durationDays > 0) {
      System.currentTimeMillis() + durationDays * 24L * 60 * 60 * 1000
    } else {
      null
    }
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(
            isPaused = true,
            pauseUntilDate = pauseUntil,
            pausedAt = System.currentTimeMillis()
          )
        } else habit
      }
    }
  }

  fun resumeHabit(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(
            isPaused = false,
            pauseUntilDate = null,
            pausedAt = null
          )
        } else habit
      }
    }
  }

  fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
    val newTask = Task(
      id = UUID.randomUUID().toString(),
      title = title,
      priority = priority,
      isCompleted = false,
      isGoalTask = false,
      schedule = TaskSchedule.TODAY
    )
    _generalTasks.update { listOf(newTask) + it }
  }

  fun addGeneralTask(
    title: String,
    notes: String? = null,
    priority: Priority = Priority.MEDIUM,
    schedule: TaskSchedule = TaskSchedule.TODAY,
    dueDateFormatted: String? = null
  ) {
    val newTask = Task(
      id = UUID.randomUUID().toString(),
      title = title.trim(),
      notes = notes?.trim()?.ifEmpty { null },
      priority = priority,
      isCompleted = false,
      isGoalTask = false,
      schedule = schedule,
      dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null },
      createdAt = System.currentTimeMillis()
    )
    _generalTasks.update { listOf(newTask) + it }
  }

  fun updateGeneralTask(
    taskId: String,
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    _generalTasks.update { list ->
      list.map { task ->
        if (task.id == taskId) {
          task.copy(
            title = title.trim(),
            notes = notes?.trim()?.ifEmpty { null },
            priority = priority,
            schedule = schedule,
            dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null }
          )
        } else task
      }
    }
  }

  fun deleteGeneralTask(taskId: String) {
    _generalTasks.update { list -> list.filterNot { it.id == taskId } }
  }

  fun addNote(
    title: String,
    content: String,
    colorLong: Long = 0xFFFFFBEB,
    tag: String = "أفكار عامة",
    isPinned: Boolean = false
  ) {
    val newNote = Note(
      id = UUID.randomUUID().toString(),
      title = title.trim(),
      content = content.trim(),
      colorLong = colorLong,
      tag = tag,
      isPinned = isPinned,
      createdAt = System.currentTimeMillis(),
      updatedAt = System.currentTimeMillis()
    )
    _notes.update { listOf(newNote) + it }
  }

  fun updateNote(
    noteId: String,
    title: String,
    content: String,
    colorLong: Long,
    tag: String,
    isPinned: Boolean
  ) {
    _notes.update { list ->
      list.map { note ->
        if (note.id == noteId) {
          note.copy(
            title = title.trim(),
            content = content.trim(),
            colorLong = colorLong,
            tag = tag,
            isPinned = isPinned,
            updatedAt = System.currentTimeMillis()
          )
        } else note
      }
    }
  }

  fun togglePinNote(noteId: String) {
    _notes.update { list ->
      list.map { note ->
        if (note.id == noteId) {
          note.copy(isPinned = !note.isPinned)
        } else note
      }
    }
  }

  fun deleteNote(noteId: String) {
    _notes.update { list -> list.filterNot { it.id == noteId } }
  }

  fun postponeGeneralTask(taskId: String) {
    val tomorrow = getTomorrowDateString()
    val today = getTodayDateString()
    _generalTasks.update { list ->
      list.map { task ->
        if (task.id == taskId) {
          task.copy(
            schedule = TaskSchedule.TOMORROW,
            dueDateFormatted = tomorrow,
            isPostponed = true,
            postponedCount = task.postponedCount + 1,
            postponedFromDate = today
          )
        } else task
      }
    }
  }

  fun postponeGoalTask(goalId: String, taskId: String) {
    val tomorrow = getTomorrowDateString()
    val today = getTodayDateString()
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          val updatedTasks = goal.tasks.map { task ->
            if (task.id == taskId) {
              task.copy(
                schedule = TaskSchedule.TOMORROW,
                dueDateFormatted = tomorrow,
                isPostponed = true,
                postponedCount = task.postponedCount + 1,
                postponedFromDate = today
              )
            } else task
          }
          goal.copy(tasks = updatedTasks)
        } else goal
      }
    }
  }

  // Pre-configured historical daily logs for past days to demonstrate comprehensive tracking
  private val _dailyHistory = MutableStateFlow<Map<String, DaySummaryHistory>>(
    createInitialPastDailyHistory()
  )
  val dailyHistory: StateFlow<Map<String, DaySummaryHistory>> = _dailyHistory.asStateFlow()

  fun getDaySummary(dateKey: String): DaySummaryHistory {
    val todayKey = getTodayDateString()
    val cleanDateKey = dateKey.trim().replace('-', '/')

    // 1. If today: calculate live in real-time from active habits, tasks, and goals
    if (cleanDateKey == todayKey) {
      val todayDay = getCurrentDayOfWeekArabic()
      val habitList = _habits.value
      val todayScheduledHabits = habitList.filter { it.isScheduledForToday(todayDay) }

      val habitRecords = todayScheduledHabits.map { habit ->
        val isDone = habit.isCompleted
        val note = when {
          isDone -> "مكتمل بالكامل 🌟"
          habit.currentValue > 0 -> "أنجزت ${habit.currentValue} من ${habit.targetValue} ${habit.unit}"
          else -> "لم يُنجز بعد"
        }
        DayHabitRecord(
          habitId = habit.id,
          title = habit.title,
          iconEmoji = habit.iconEmoji,
          targetValue = habit.targetValue,
          actualValue = habit.currentValue,
          unit = habit.unit,
          type = habit.type,
          isMandatory = habit.isMandatory,
          isCompleted = isDone,
          statusNote = note
        )
      }

      val todayGeneralTasks = _generalTasks.value.filter { it.isDueToday }.map {
        DayTaskRecord(task = it, isGoalTask = false)
      }

      val todayGoalTasks = _goals.value.flatMap { goal ->
        goal.todayTasks.map { task ->
          DayTaskRecord(
            task = task,
            isGoalTask = true,
            goalId = goal.id,
            goalTitle = goal.title,
            goalIconId = goal.iconId
          )
        }
      }

      return DaySummaryHistory(
        dateKey = todayKey,
        dateDisplayArabic = formatArabicDisplayDateFromKey(todayKey),
        dayOfWeekArabic = todayDay,
        habits = habitRecords,
        tasks = todayGeneralTasks + todayGoalTasks,
        isToday = true,
        isPast = false,
        isFuture = false
      )
    }

    // 2. If existing historical record:
    _dailyHistory.value[cleanDateKey]?.let { return it }

    // 3. For any other date (arbitrary past or future): compute dynamic representation
    val cal = Calendar.getInstance()
    try {
      val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
      cal.time = sdf.parse(cleanDateKey) ?: Date()
    } catch (_: Exception) {}

    val isPastDate = cleanDateKey < todayKey
    val dayOfWeek = getDayOfWeekArabicFromCalendar(cal)

    // Habits scheduled for that day
    val habitRecords = _habits.value.filter { it.isScheduledForToday(dayOfWeek) }.map { habit ->
      DayHabitRecord(
        habitId = habit.id,
        title = habit.title,
        iconEmoji = habit.iconEmoji,
        targetValue = habit.targetValue,
        actualValue = if (isPastDate) habit.targetValue else 0,
        unit = habit.unit,
        type = habit.type,
        isMandatory = habit.isMandatory,
        isCompleted = isPastDate,
        statusNote = if (isPastDate) "مكتمل في هذا اليوم" else "مجدول"
      )
    }

    val matchedGeneralTasks = _generalTasks.value.filter { task ->
      val taskClean = task.dueDateFormatted?.trim()?.replace('-', '/')
      taskClean == cleanDateKey
    }.map { DayTaskRecord(task = it, isGoalTask = false) }

    val matchedGoalTasks = _goals.value.flatMap { goal ->
      goal.tasks.filter { task ->
        val taskClean = task.dueDateFormatted?.trim()?.replace('-', '/')
        taskClean == cleanDateKey
      }.map { task ->
        DayTaskRecord(
          task = task,
          isGoalTask = true,
          goalId = goal.id,
          goalTitle = goal.title,
          goalIconId = goal.iconId
        )
      }
    }

    return DaySummaryHistory(
      dateKey = cleanDateKey,
      dateDisplayArabic = formatArabicDisplayDateFromKey(cleanDateKey),
      dayOfWeekArabic = dayOfWeek,
      habits = habitRecords,
      tasks = matchedGeneralTasks + matchedGoalTasks,
      isToday = false,
      isPast = isPastDate,
      isFuture = cleanDateKey > todayKey
    )
  }

  fun calculateAnalytics(): DailyAnalytics {
    val habitList = _habits.value
    val taskList = _generalTasks.value
    val activeGoalList = _goals.value.filter { !it.isPaused }

    // Today's goals progress is calculated from actual today's scheduled goal tasks!
    val todayGoalTasks = activeGoalList.flatMap { it.todayTasks }
    val totalTodayGoalTasks = todayGoalTasks.size
    val completedTodayGoalTasks = todayGoalTasks.count { it.isCompleted }

    val todayDay = getCurrentDayOfWeekArabic()
    val todayHabits = if (_user.value.isRestModeActive) {
      habitList.filter { it.isScheduledForToday(todayDay) && it.isMandatory }
    } else {
      habitList.filter { it.isScheduledForToday(todayDay) }
    }

    // Only general tasks scheduled for TODAY are counted in daily analytics!
    val todayGeneralTasks = taskList.filter { it.isDueToday }

    return DailyAnalytics(
      habitsCompleted = todayHabits.count { it.isCompleted },
      habitsTotal = todayHabits.size,
      tasksCompleted = todayGeneralTasks.count { it.isCompleted },
      tasksTotal = todayGeneralTasks.size,
      goalsCompleted = completedTodayGoalTasks,
      goalsTotal = totalTodayGoalTasks
    )
  }
}

fun getDateOffsetKey(daysOffset: Int): String {
  val cal = Calendar.getInstance()
  cal.add(Calendar.DAY_OF_YEAR, daysOffset)
  return SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(cal.time)
}

fun formatArabicDisplayDateFromKey(dateKey: String): String {
  return try {
    val clean = dateKey.trim().replace('-', '/')
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
    val date = sdf.parse(clean) ?: Date()
    val cal = Calendar.getInstance().apply { time = date }
    val dayOfWeek = when (cal.get(Calendar.DAY_OF_WEEK)) {
      Calendar.SATURDAY -> "السبت"
      Calendar.SUNDAY -> "الأحد"
      Calendar.MONDAY -> "الإثنين"
      Calendar.TUESDAY -> "الثلاثاء"
      Calendar.WEDNESDAY -> "الأربعاء"
      Calendar.THURSDAY -> "الخميس"
      Calendar.FRIDAY -> "الجمعة"
      else -> ""
    }
    val monthsArabic = listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val month = monthsArabic[cal.get(Calendar.MONTH)]
    val year = cal.get(Calendar.YEAR)
    "$dayOfWeek، $day $month $year"
  } catch (e: Exception) {
    dateKey
  }
}

fun getDayOfWeekArabicFromCalendar(cal: Calendar): DayOfWeekArabic {
  return when (cal.get(Calendar.DAY_OF_WEEK)) {
    Calendar.SATURDAY -> DayOfWeekArabic.SATURDAY
    Calendar.SUNDAY -> DayOfWeekArabic.SUNDAY
    Calendar.MONDAY -> DayOfWeekArabic.MONDAY
    Calendar.TUESDAY -> DayOfWeekArabic.TUESDAY
    Calendar.WEDNESDAY -> DayOfWeekArabic.WEDNESDAY
    Calendar.THURSDAY -> DayOfWeekArabic.THURSDAY
    Calendar.FRIDAY -> DayOfWeekArabic.FRIDAY
    else -> DayOfWeekArabic.SUNDAY
  }
}

private fun createInitialPastDailyHistory(): Map<String, DaySummaryHistory> {
  val map = mutableMapOf<String, DaySummaryHistory>()

  // 1. Yesterday (أمس): demonstrating partial completion, missed gym, and partial reading!
  val yestKey = getDateOffsetKey(-1)
  val yestCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
  map[yestKey] = DaySummaryHistory(
    dateKey = yestKey,
    dateDisplayArabic = formatArabicDisplayDateFromKey(yestKey),
    dayOfWeekArabic = getDayOfWeekArabicFromCalendar(yestCal),
    isToday = false,
    isPast = true,
    isFuture = false,
    habits = listOf(
      DayHabitRecord(
        habitId = "h1",
        title = "الصلوات الخمس",
        iconEmoji = "🕌",
        targetValue = 5,
        actualValue = 5,
        unit = "صلوات",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = true,
        statusNote = "مكتمل بالكامل 5 صلوات 🌟"
      ),
      DayHabitRecord(
        habitId = "h3",
        title = "الورد القرآني",
        iconEmoji = "📖",
        targetValue = 10,
        actualValue = 7,
        unit = "صفحة",
        type = HabitType.QUANTITY,
        isMandatory = true,
        isCompleted = false,
        statusNote = "تم قراءة 7 من 10 صفحات (70%) ⏳"
      ),
      DayHabitRecord(
        habitId = "h2",
        title = "الذهاب للجيم (تمارين رياضية)",
        iconEmoji = "🏋️‍♂️",
        targetValue = 60,
        actualValue = 0,
        unit = "دقيقة",
        type = HabitType.DURATION,
        isMandatory = false,
        isCompleted = false,
        statusNote = "لم يتم الذهاب للجيم (0 من 60 دقيقة) ❌"
      ),
      DayHabitRecord(
        habitId = "h4",
        title = "التدرب على الكيبورد",
        iconEmoji = "⌨️",
        targetValue = 30,
        actualValue = 30,
        unit = "دقيقة",
        type = HabitType.DURATION,
        isMandatory = false,
        isCompleted = true,
        statusNote = "تم إنجاز 30 دقيقة تدريب ⌨️"
      ),
      DayHabitRecord(
        habitId = "h6",
        title = "شرب الماء",
        iconEmoji = "💧",
        targetValue = 8,
        actualValue = 8,
        unit = "أكواب",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = true,
        statusNote = "8 من 8 أكواب ماء 💧"
      ),
      DayHabitRecord(
        habitId = "h7",
        title = "أذكار الصباح والمساء",
        iconEmoji = "✨",
        targetValue = 1,
        actualValue = 1,
        unit = "مرة",
        type = HabitType.BOOLEAN,
        isMandatory = true,
        isCompleted = true,
        statusNote = "تمت قراءة الأذكار بنجاح ✨"
      )
    ),
    tasks = listOf(
      DayTaskRecord(
        task = Task(
          id = "past_t1",
          title = "إنهاء مسودة العرض التقديمي",
          priority = Priority.HIGH,
          isCompleted = true,
          dueDateFormatted = yestKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "past_t2",
          title = "إرسال التقرير الأسبوعي للإدارة",
          priority = Priority.MEDIUM,
          isCompleted = true,
          dueDateFormatted = yestKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "past_t3",
          title = "مراجعة عقود الصيانة وتحديث السجلات",
          priority = Priority.LOW,
          isCompleted = false,
          isPostponed = true,
          postponedCount = 1,
          postponedFromDate = yestKey,
          notes = "تم ترحيل المهمة للغد لضيق الوقت ➡️",
          dueDateFormatted = yestKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "past_gt1",
          title = "حفظ 20 كلمة جديدة بالإنجليزية",
          priority = Priority.HIGH,
          isCompleted = true,
          goalId = "g1",
          isGoalTask = true,
          dueDateFormatted = yestKey
        ),
        isGoalTask = true,
        goalId = "g1",
        goalTitle = "إتقان اللغة الإنجليزية",
        goalIconId = "book"
      ),
      DayTaskRecord(
        task = Task(
          id = "past_gt2",
          title = "تصميم واجهة لوحة التحكم",
          priority = Priority.HIGH,
          isCompleted = true,
          goalId = "g2",
          isGoalTask = true,
          dueDateFormatted = yestKey
        ),
        isGoalTask = true,
        goalId = "g2",
        goalTitle = "تطوير المهارات البرمجية",
        goalIconId = "code"
      )
    )
  )

  // 2. Two days ago (قبل يومين): High achievement day
  val twoDaysKey = getDateOffsetKey(-2)
  val twoDaysCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }
  map[twoDaysKey] = DaySummaryHistory(
    dateKey = twoDaysKey,
    dateDisplayArabic = formatArabicDisplayDateFromKey(twoDaysKey),
    dayOfWeekArabic = getDayOfWeekArabicFromCalendar(twoDaysCal),
    isToday = false,
    isPast = true,
    isFuture = false,
    habits = listOf(
      DayHabitRecord(
        habitId = "h1",
        title = "الصلوات الخمس",
        iconEmoji = "🕌",
        targetValue = 5,
        actualValue = 5,
        unit = "صلوات",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = true,
        statusNote = "مكتمل بالكامل 5 صلوات 🌟"
      ),
      DayHabitRecord(
        habitId = "h3",
        title = "الورد القرآني",
        iconEmoji = "📖",
        targetValue = 10,
        actualValue = 10,
        unit = "صفحة",
        type = HabitType.QUANTITY,
        isMandatory = true,
        isCompleted = true,
        statusNote = "تم قراءة 10 من 10 صفحات (100%) 🌟"
      ),
      DayHabitRecord(
        habitId = "h4",
        title = "التدرب على الكيبورد",
        iconEmoji = "⌨️",
        targetValue = 30,
        actualValue = 30,
        unit = "دقيقة",
        type = HabitType.DURATION,
        isMandatory = false,
        isCompleted = true,
        statusNote = "أكملت 30 دقيقة تدريب ⌨️"
      ),
      DayHabitRecord(
        habitId = "h5",
        title = "تمارين الضغط",
        iconEmoji = "💪",
        targetValue = 50,
        actualValue = 50,
        unit = "عدة",
        type = HabitType.QUANTITY,
        isMandatory = false,
        isCompleted = true,
        statusNote = "50 عدة ضغط 💪"
      ),
      DayHabitRecord(
        habitId = "h6",
        title = "شرب الماء",
        iconEmoji = "💧",
        targetValue = 8,
        actualValue = 7,
        unit = "أكواب",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = false,
        statusNote = "7 من 8 أكواب ماء"
      ),
      DayHabitRecord(
        habitId = "h7",
        title = "أذكار الصباح والمساء",
        iconEmoji = "✨",
        targetValue = 1,
        actualValue = 1,
        unit = "مرة",
        type = HabitType.BOOLEAN,
        isMandatory = true,
        isCompleted = true,
        statusNote = "تمت قراءة الأذكار ✨"
      )
    ),
    tasks = listOf(
      DayTaskRecord(
        task = Task(
          id = "d2_t1",
          title = "تنظيم ملفات المشروع",
          priority = Priority.MEDIUM,
          isCompleted = true,
          dueDateFormatted = twoDaysKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "d2_t2",
          title = "الاتصال بالعميل لمناقشة المتطلبات",
          priority = Priority.HIGH,
          isCompleted = true,
          dueDateFormatted = twoDaysKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "d2_gt1",
          title = "جلسة استطالة ومساج",
          priority = Priority.LOW,
          isCompleted = true,
          goalId = "g3",
          isGoalTask = true,
          dueDateFormatted = twoDaysKey
        ),
        isGoalTask = true,
        goalId = "g3",
        goalTitle = "تحسين اللياقة البدنية",
        goalIconId = "fitness"
      )
    )
  )

  // 3. Three days ago (قبل 3 أيام)
  val threeDaysKey = getDateOffsetKey(-3)
  val threeDaysCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }
  map[threeDaysKey] = DaySummaryHistory(
    dateKey = threeDaysKey,
    dateDisplayArabic = formatArabicDisplayDateFromKey(threeDaysKey),
    dayOfWeekArabic = getDayOfWeekArabicFromCalendar(threeDaysCal),
    isToday = false,
    isPast = true,
    isFuture = false,
    habits = listOf(
      DayHabitRecord(
        habitId = "h1",
        title = "الصلوات الخمس",
        iconEmoji = "🕌",
        targetValue = 5,
        actualValue = 5,
        unit = "صلوات",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = true,
        statusNote = "مكتمل بالكامل 5 صلوات 🕌"
      ),
      DayHabitRecord(
        habitId = "h3",
        title = "الورد القرآني",
        iconEmoji = "📖",
        targetValue = 10,
        actualValue = 10,
        unit = "صفحة",
        type = HabitType.QUANTITY,
        isMandatory = true,
        isCompleted = true,
        statusNote = "10 من 10 صفحات 📖"
      ),
      DayHabitRecord(
        habitId = "h6",
        title = "شرب الماء",
        iconEmoji = "💧",
        targetValue = 8,
        actualValue = 8,
        unit = "أكواب",
        type = HabitType.COUNTER,
        isMandatory = true,
        isCompleted = true,
        statusNote = "8 أكواب ماء 💧"
      ),
      DayHabitRecord(
        habitId = "h7",
        title = "أذكار الصباح والمساء",
        iconEmoji = "✨",
        targetValue = 1,
        actualValue = 1,
        unit = "مرة",
        type = HabitType.BOOLEAN,
        isMandatory = true,
        isCompleted = true,
        statusNote = "تمت قراءة الأذكار ✨"
      )
    ),
    tasks = listOf(
      DayTaskRecord(
        task = Task(
          id = "d3_t1",
          title = "مراجعة الكود البرمجي الخاص بالمستودع",
          priority = Priority.HIGH,
          isCompleted = true,
          dueDateFormatted = threeDaysKey
        ),
        isGoalTask = false
      ),
      DayTaskRecord(
        task = Task(
          id = "d3_t2",
          title = "شراء المستلزمات المكتبية",
          priority = Priority.LOW,
          isCompleted = false,
          notes = "لم يتسع الوقت لزيارة المتجر ❌",
          dueDateFormatted = threeDaysKey
        ),
        isGoalTask = false
      )
    )
  )

  return map
}

