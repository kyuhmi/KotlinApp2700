package org.kym.todoapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.kym.todoapp.data.DEFAULT_POMODORO_SETTINGS
import org.kym.todoapp.data.PomodoroSettings

class PomodoroSettingsViewModel : ViewModel() {
    private val _settings = MutableStateFlow(DEFAULT_POMODORO_SETTINGS)
    val settings: StateFlow<PomodoroSettings> = _settings.asStateFlow()

    // Initialize with current settings from parent ViewModel
    fun initializeWith(currentSettings: PomodoroSettings) {
        _settings.value = currentSettings
    }

    // Methods to update individual settings
    fun updateWorkDuration(minutes: Int, seconds: Int) {
        val durationMs = (minutes * 60 + seconds) * 1000L
        _settings.update { it.copy(workDuration = durationMs) }
    }

    fun updateShortBreakDuration(minutes: Int, seconds: Int) {
        val durationMs = (minutes * 60 + seconds) * 1000L
        _settings.update { it.copy(shortBreakDuration = durationMs) }
    }

    fun updateLongBreakDuration(minutes: Int, seconds: Int) {
        val durationMs = (minutes * 60 + seconds) * 1000L
        _settings.update { it.copy(longBreakDuration = durationMs) }
    }

    fun updateLongBreakInterval(interval: Int) {
        _settings.update { it.copy(longBreakInterval = interval) }
    }

    // Get the current settings value
    fun getCurrentSettings(): PomodoroSettings = _settings.value
}
