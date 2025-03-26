package org.kym.todoapp.ui

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TimePickerWidget(
    minutes: Int,
    seconds: Int,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    // local state for text fields
    var minutesText by remember { mutableStateOf(minutes.toString().padStart(2, '0')) }
    var secondsText by remember { mutableStateOf(seconds.toString().padStart(2, '0')) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // minutes picker
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            IconButton(
                onClick = {
                    val newMinutes = (minutes + 1).coerceAtMost(99)
                    onMinutesChanged(newMinutes)
                    minutesText = newMinutes.toString().padStart(2, '0')
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Minutes")
            }

            OutlinedTextField(
                value = minutesText,
                onValueChange = { input ->
                    // only accept digits and limit to 2 chars
                    if (input.length <= 2 && input.all { it.isDigit() }) {
                        minutesText = input
                        if (input.isNotEmpty()) {
                            val newMinutes = input.toInt().coerceIn(0, 99)
                            if (newMinutes != minutes) {
                                onMinutesChanged(newMinutes)
                            }
                        }
                    }
                },
                modifier = Modifier.width(70.dp),
                textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = {
                    val newMinutes = (minutes - 1).coerceAtLeast(0)
                    onMinutesChanged(newMinutes)
                    minutesText = newMinutes.toString().padStart(2, '0')
                }
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Minutes")
            }

            Text("min", fontSize = 16.sp)
        }

        Text(":", fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))

        // Seconds picker
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            IconButton(
                onClick = {
                    val newSeconds = (seconds + 1).coerceAtMost(59)
                    onSecondsChanged(newSeconds)
                    secondsText = newSeconds.toString().padStart(2, '0')
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Seconds")
            }

            OutlinedTextField(
                value = secondsText,
                onValueChange = { input ->
                    // Only accept digits and limit to 2 characters
                    if (input.length <= 2 && input.all { it.isDigit() }) {
                        secondsText = input
                        if (input.isNotEmpty()) {
                            val newSeconds = input.toInt().coerceIn(0, 59)
                            if (newSeconds != seconds) {
                                onSecondsChanged(newSeconds)
                            }
                        }
                    }
                },
                modifier = Modifier.width(70.dp),
                textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = {
                    val newSeconds = (seconds - 1).coerceAtLeast(0)
                    onSecondsChanged(newSeconds)
                    secondsText = newSeconds.toString().padStart(2, '0')
                }
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Seconds")
            }

            Text("sec", fontSize = 16.sp)
        }
    }
}