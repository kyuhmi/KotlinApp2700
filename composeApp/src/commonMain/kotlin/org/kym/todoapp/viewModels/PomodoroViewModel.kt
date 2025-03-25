package org.kym.todoapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kym.todoapp.data.PomodoroPhase
import org.kym.todoapp.data.PomodoroTimerState
import org.kym.todoapp.data.TimerState

class PomodoroViewModel() : ViewModel() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + Job()) // Use Dispatchers.Main.immediate if on main thread

    private val _timerState = MutableStateFlow(PomodoroTimerState())
    val timerState: StateFlow<PomodoroTimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    // useful function aliases for starting specific states
    fun startWork() = startPhase(PomodoroPhase.WORK)
    fun startShortBreak() = startPhase(PomodoroPhase.SHORT_BREAK)
    fun startLongBreak() = startPhase(PomodoroPhase.LONG_BREAK)

    // start timer with specified phase
    fun startPhase(pomodoroPhase: PomodoroPhase = PomodoroPhase.WORK) { //Default to start work
        when (_timerState.value.timerState) {
            TimerState.PAUSED -> {
                // resume from paused state without changing pomo phase
                _timerState.update { it.copy(timerState = TimerState.RUNNING) }
            }
            else -> {
                // start a new timer with specified phase
                _timerState.update {
                    it.copy(
                        timerState = TimerState.RUNNING,
                        currentPhase = pomodoroPhase,
                        timeRemaining = getDurationForPhase(pomodoroPhase)
                    )
                }
            }
        }
        startTimerCoroutine() // start timer coroutine
    }

    // helper function to start the timer coroutine
    private fun startTimerCoroutine() {
        timerJob?.cancel() // Cancel any existing timer
        timerJob = viewModelScope.launch {
            while (_timerState.value.timeRemaining > 0 && _timerState.value.timerState == TimerState.RUNNING) {
                delay(1000) // Check every second
                _timerState.update {
                    it.copy(timeRemaining = it.timeRemaining - 1000) // decrement remaining time by 1 second
                }
            }
            if (_timerState.value.timerState == TimerState.RUNNING) {
                onTimerEnd() // Call a function to handle timer completion
            }
        }
    }

    fun pause() {
        _timerState.update { it.copy(timerState = TimerState.PAUSED) }
        timerJob?.cancel()
    }

    fun stop() {
        _timerState.update {
            it.copy(
                timerState = TimerState.STOPPED,
                currentPhase = PomodoroPhase.WORK,
                timeRemaining = 0L
            )
        } // Reset to work
        timerJob?.cancel()
    }

    fun resetPomos() {
        stop()
        _timerState.update { it.copy(pomodoroCount = 0) }
    }

    private fun onTimerEnd() {
        when (_timerState.value.currentPhase) {
            // case for when a work session ends
            PomodoroPhase.WORK -> {
                // Increment pomodoro count and determine next state
                val updatedCount = _timerState.value.pomodoroCount + 1
                val nextPhase =
                    if (updatedCount % _timerState.value.settings.longBreakInterval == 0) {
                        PomodoroPhase.LONG_BREAK
                    } else {
                        PomodoroPhase.SHORT_BREAK
                    }
                // Update state and start the next timer
                _timerState.update {
                    it.copy(
                        pomodoroCount = updatedCount,
                        timerState = TimerState.STOPPED // reset to stopped before starting next phase
                    )
                }
                startPhase(nextPhase) // Auto-start the next break timer
            }
            // case for when a break session ends
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> {
                // Transition back to WORK after a break
                _timerState.update {
                    it.copy(
                        timerState = TimerState.STOPPED // reset to stopped before starting next phase
                    )
                }
                startPhase(PomodoroPhase.WORK) // Auto-start the next work timer
            }
            // do nothing for other states
            else -> {}
        }
    }

    private fun getDurationForPhase(state: PomodoroPhase): Long {
        return when (state) {
            PomodoroPhase.WORK -> _timerState.value.settings.workDuration
            PomodoroPhase.SHORT_BREAK -> _timerState.value.settings.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> _timerState.value.settings.longBreakDuration
            else -> 0L // Or some default value, case shouldn't really happen
        }
    }

    //Cancel timer if the viewmodel clears
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
