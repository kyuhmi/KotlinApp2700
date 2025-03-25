package org.kym.todoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.data.PomodoroPhase
import org.kym.todoapp.data.PomodoroTimerState
import org.kym.todoapp.viewModels.PomodoroViewModel
import org.kym.todoapp.data.TimerState

@Composable
fun PomodoroScreen(navController: NavController, viewModel: PomodoroViewModel = koinViewModel<PomodoroViewModel>()) {
    val timerState by viewModel.timerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimerDisplay(timerState.timeRemaining)
        PomodoroStatus(timerState)
        Spacer(modifier = Modifier.height(16.dp))
        Controls(
            timerState = timerState,
            onStart = { viewModel.startPhase() },
            onPause = { viewModel.pause() },
            onStop = { viewModel.stop() },
            onStartWork = {viewModel.startWork()},
            onStartShortBreak = {viewModel.startShortBreak()},
            onStartLongBreak = {viewModel.startLongBreak()},
            onResetPomos = {viewModel.resetPomos()},
            onSkipPhaseAndStart = {viewModel.skipPhaseAndStart()}
        )
        Text(text = "Pomodoros Completed: ${timerState.pomodoroCount}", fontSize = 16.sp)
    }
}
@Composable
fun PomodoroStatus(timerState: PomodoroTimerState){
    val phaseText = when(timerState.timerState) {
        TimerState.RUNNING -> "Running"
        TimerState.PAUSED -> "Paused"
        TimerState.STOPPED -> "Stopped"
    }
    val statusText = when(timerState.currentPhase){
        PomodoroPhase.WORK -> "Work"
        PomodoroPhase.SHORT_BREAK -> "Short Break"
        PomodoroPhase.LONG_BREAK -> "Long Break"
    }
    Text("$phaseText: $statusText", fontSize = 24.sp)
}

@Composable
fun TimerDisplay(timeRemaining: Long) {
    val minutes = (timeRemaining / 1000) / 60
    val seconds = (timeRemaining / 1000) % 60
    Text(
        text = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
        fontSize = 48.sp
    )
}

@Composable
fun Controls(
    timerState: PomodoroTimerState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onStartWork: () -> Unit,
    onStartShortBreak: () -> Unit,
    onStartLongBreak: () -> Unit,
    onResetPomos: () -> Unit,
    onSkipPhaseAndStart: () -> Unit
) {
    val horizontalArrangement = Arrangement.spacedBy(8.dp)
    val verticalAlignment = Alignment.CenterVertically
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        if (timerState.timerState == TimerState.RUNNING) {
            IconButton(onClick = onPause) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(Icons.Filled.Pause, contentDescription = "Pause")
                    Text("Pause", fontSize = 8.sp)
                }
            }
        } else {
            IconButton(onClick = onStart) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
                    Text(if (timerState.timerState == TimerState.PAUSED) "Resume" else "Start", fontSize = 8.sp)
                }

            }
        }
        IconButton(onClick = onStop) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Stop, contentDescription = "Stop")
                Text("Stop", fontSize = 8.sp)
            }
        }
        IconButton(onClick = onStartWork) {
            Text("Work")
        }
        IconButton(onClick = onStartShortBreak) {
            Text("Short Break")
        }
        IconButton(onClick = onStartLongBreak) {
            Text("Long Break")
        }
    }
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        IconButton(onClick = onResetPomos) {
            Text("Reset")
        }
        IconButton(onClick = onSkipPhaseAndStart) {
            Text("Skip")
        }
    }
}


@Preview
@Composable
fun PomodoroScreenPreview() {
    MaterialTheme {
        PomodoroScreen(navController = rememberNavController())
    }
}