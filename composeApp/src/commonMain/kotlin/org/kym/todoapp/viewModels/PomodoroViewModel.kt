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

    fun start() {
        // only start if not already running
        if (_timerState.value.timerState != TimerState.RUNNING) {
            // if the method is called when timer is not acked, implied that we want to ack it.
            if (_timerState.value.isWaitingForAcknowledgement) {
                acknowledgeAlarm()
            }
            // resume if paused, otherwise start from current phase
            _timerState.update { it.copy(timerState = TimerState.RUNNING) }
            startTimerCoroutine()
        }
    }

    // useful function aliases for starting specific states
    fun startWork() = startPhase(PomodoroPhase.WORK)
    fun startShortBreak() = startPhase(PomodoroPhase.SHORT_BREAK)
    fun startLongBreak() = startPhase(PomodoroPhase.LONG_BREAK)

    // start timer with specified phase
    private fun startPhase(pomodoroPhase: PomodoroPhase = PomodoroPhase.WORK) { //Default to start work
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
                timeRemaining = _timerState.value.settings.workDuration
            )
        } // Reset to work
        timerJob?.cancel()
    }

    fun resetPomos() {
        stop()
        _timerState.update { it.copy(pomodoroCount = 0) }
    }

    fun skipPhaseAndStart() {
        val nextPhase = determineNextPhase(_timerState.value.currentPhase, _timerState.value.pomodoroCount)
        val nextCount = updatePomodoroCount(_timerState.value.currentPhase, _timerState.value.pomodoroCount)

        // update count: act as if current phase is completed
        _timerState.update { it.copy(pomodoroCount = nextCount) }

        startPhase(nextPhase)
    }

    private fun onTimerEnd() {
        playAlarm() // play alarm sound

        // set the state to waiting for acknowledgement
        _timerState.update {
            it.copy(
                timerState = TimerState.STOPPED,
                isWaitingForAcknowledgement = true
            )
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

    private fun playAlarm() {
        // TODO: Implement alarm sound playback
    }

    // function for acknowledging the alarm and preparing timer for next phase
    fun acknowledgeAlarm() {
        // if not waiting for acknowledgement, do nothing
        if (!_timerState.value.isWaitingForAcknowledgement) return

        // Determine next phase without starting it
        val nextPhase = determineNextPhase(_timerState.value.currentPhase, _timerState.value.pomodoroCount)
        val nextCount = updatePomodoroCount(_timerState.value.currentPhase, _timerState.value.pomodoroCount)

        // Update state to prepare for next phase
        // Timer should already be stopped by onTimerEnd()
        _timerState.update {
            it.copy(
                currentPhase = nextPhase,
                pomodoroCount = nextCount,
                timeRemaining = getDurationForPhase(nextPhase),
                isWaitingForAcknowledgement = false
            )
        }
    }

    // function for updating pomodoro count based on current phase
    private fun updatePomodoroCount(currentPhase: PomodoroPhase, currentCount: Int): Int {
        return if (currentPhase == PomodoroPhase.WORK) currentCount + 1 else currentCount
    }

    // function for determining the next phase based on current phase and current pomodoro count
    private fun determineNextPhase(currentPhase: PomodoroPhase, currentPomoCount: Int): PomodoroPhase {
        return when (currentPhase) {
            PomodoroPhase.WORK -> {
                // assuming that this is called when the work phase is over, so implicitly
                // there is one more completed pomodoro
                val completedPomodoros = currentPomoCount + 1
                if (completedPomodoros % _timerState.value.settings.longBreakInterval == 0) {
                    PomodoroPhase.LONG_BREAK
                } else {
                    PomodoroPhase.SHORT_BREAK
                }
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.WORK
        }
    }

    //Cancel timer if the viewmodel clears
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
