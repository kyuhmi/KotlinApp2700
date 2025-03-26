package org.kym.todoapp.ui

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.data.PomodoroSettings
import org.kym.todoapp.viewModels.PomodoroSettingsViewModel
import org.kym.todoapp.viewModels.PomodoroViewModel

@Composable
fun PomodoroSettingsScreen(
    navController: NavController,
    parentViewModel: PomodoroViewModel = koinViewModel(
        viewModelStoreOwner = remember { navController.getBackStackEntry(Screens.Pomodoro.route) } // get parent viewmodel from backstack
    ),
    settingsViewModel: PomodoroSettingsViewModel = koinViewModel()
) {
    // scrollable state
    val scrollState = rememberScrollState()

    // get parent timer state
    val timerState by parentViewModel.timerState.collectAsState()

    // initialize settings viewmodel with current settings from parent timer state each time the screen is recomposed
    settingsViewModel.initializeWith(timerState.settings)

    // collect settings from initialized settings viewmodel
    val currentSettings by settingsViewModel.settings.collectAsState()

    // using values from the initialized settings viewmodel
    // Convert milliseconds to minutes and seconds for initial values
    var workMinutes by remember { mutableStateOf((currentSettings.workDuration / 1000 / 60).toInt()) }
    var workSeconds by remember { mutableStateOf(((currentSettings.workDuration / 1000) % 60).toInt()) }

    var shortBreakMinutes by remember { mutableStateOf((currentSettings.shortBreakDuration / 1000 / 60).toInt()) }
    var shortBreakSeconds by remember { mutableStateOf(((currentSettings.shortBreakDuration / 1000) % 60).toInt()) }

    var longBreakMinutes by remember { mutableStateOf((currentSettings.longBreakDuration / 1000 / 60).toInt()) }
    var longBreakSeconds by remember { mutableStateOf(((currentSettings.longBreakDuration / 1000) % 60).toInt()) }

    var longBreakInterval by remember { mutableStateOf(currentSettings.longBreakInterval.toString()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            // todo: figure this out
            Text(text = "Pomodoro Settings", fontSize = 24.sp)
            Spacer(Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Work Duration timer picker
        Text("Work Duration", fontSize = 22.sp)
        TimePickerWidget(
            minutes = workMinutes,
            seconds = workSeconds,
            onMinutesChanged = { workMinutes = it },
            onSecondsChanged = { workSeconds = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Short Break Duration timer picker
        Text("Short Break Duration", fontSize = 22.sp)
        TimePickerWidget(
            minutes = shortBreakMinutes,
            seconds = shortBreakSeconds,
            onMinutesChanged = { shortBreakMinutes = it },
            onSecondsChanged = { shortBreakSeconds = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Long Break Duration timer picker
        Text("Long Break Duration", fontSize = 22.sp)
        TimePickerWidget(
            minutes = longBreakMinutes,
            seconds = longBreakSeconds,
            onMinutesChanged = { longBreakMinutes = it },
            onSecondsChanged = { longBreakSeconds = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Long Break interval
        Text("Long Break Interval (Pomodoros)", fontSize = 22.sp, modifier = Modifier.padding(bottom = 8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // button to decrease long break interval
            IconButton(
                onClick = {
                    val current = longBreakInterval.toIntOrNull() ?: currentSettings.longBreakInterval
                    if (current > 1) longBreakInterval = (current - 1).toString()
                }
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }

            // text field to change long break interval
            TextField(
                value = longBreakInterval,
                onValueChange = { longBreakInterval = it }, // update state on change
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(80.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center),
                singleLine = true
            )

            // button to increase long break interval
            IconButton(
                onClick = {
                    val current = longBreakInterval.toIntOrNull() ?: currentSettings.longBreakInterval
                    longBreakInterval = (current + 1).toString()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save button
        Button(
            onClick = {
                // update settings in settings viewmodel first
                settingsViewModel.updateWorkDuration(workMinutes, workSeconds)
                settingsViewModel.updateShortBreakDuration(shortBreakMinutes, shortBreakSeconds)
                settingsViewModel.updateLongBreakDuration(longBreakMinutes, longBreakSeconds)
                settingsViewModel.updateLongBreakInterval(longBreakInterval.toIntOrNull() ?: currentSettings.longBreakInterval)

                // update settings in parent viewmodel
                parentViewModel.updateSettings(settingsViewModel.getCurrentSettings())

                // navigate back
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Settings")
        }
    }


}