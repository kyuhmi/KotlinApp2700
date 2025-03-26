package org.kym.todoapp.data

val DEFAULT_POMODORO_SETTINGS = PomodoroSettings()

data class PomodoroSettings(
    val workDuration: Long = 25 * 60 * 1000L, // Milliseconds (25 minutes)
    val shortBreakDuration: Long = 5 * 60 * 1000L, // 5 minutes
    val longBreakDuration: Long = 15 * 60 * 1000L, // 15 minutes
    val longBreakInterval: Int = 4, // Long break after every 4 Pomodoros
//    val workDuration: Long = 5 * 1000L, // 5 seconds
//    val shortBreakDuration: Long = 3 * 1000L, // 3 seconds
//    val longBreakDuration: Long = 10 * 1000L, // 10 seconds
//    val longBreakInterval: Int = 4, // Long break after every 4 Pomodoros
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
    val timeRemaining: Long = DEFAULT_POMODORO_SETTINGS.workDuration,
    val pomodoroCount: Int = 0,
    val settings: PomodoroSettings = PomodoroSettings(),
    val isWaitingForAcknowledgement: Boolean = false
)