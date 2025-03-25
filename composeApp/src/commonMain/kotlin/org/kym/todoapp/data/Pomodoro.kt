package org.kym.todoapp.data

data class PomodoroSettings(
//    val workDuration: Long = 25 * 60 * 1000L, // Milliseconds (25 minutes)
//    val shortBreakDuration: Long = 5 * 60 * 1000L, // 5 minutes
//    val longBreakDuration: Long = 15 * 60 * 1000L, // 15 minutes
//    val longBreakInterval: Int = 4, // Long break after every 4 Pomodoros
    val workDuration: Long = 5 * 1000L, // Milliseconds (30 seconds)
    val shortBreakDuration: Long = 3 * 1000L, // 15 seconds
    val longBreakDuration: Long = 10 * 1000L, // 45 seconds
    val longBreakInterval: Int = 4, // Long break after every 4 Pomodoros
)

enum class TimerState {
    RUNNING, PAUSED, STOPPED
}

enum class PomodoroPhase {
    WORK, SHORT_BREAK, LONG_BREAK
}

//Holds the complete state
data class PomodoroTimerState(
    val timerState: TimerState = TimerState.STOPPED,
    val currentPhase: PomodoroPhase = PomodoroPhase.WORK,
    val timeRemaining: Long = 0L,
    val pomodoroCount: Int = 0,
    val settings: PomodoroSettings = PomodoroSettings()
)