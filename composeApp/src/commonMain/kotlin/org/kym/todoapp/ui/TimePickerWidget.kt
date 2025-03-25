package org.kym.todoapp.ui

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimePickerWidget(
    minutes: Int,
    seconds: Int,
    onMinutesChanged: (Int) -> Unit,
    onSecondsChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // minutes picker
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = { onMinutesChanged((minutes + 1).coerceAtMost(99)) }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Minutes")
            }

            Text(
                text = minutes.toString().padStart(2, '0'),
                fontSize = 24.sp
            )

            IconButton(
                onClick = { onMinutesChanged((minutes - 1).coerceAtLeast(0)) }
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Minutes")
            }

            Text("min", fontSize = 24.sp)
        }

        Text(":", fontSize = 24.sp)

        // Seconds picker
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = { onSecondsChanged((seconds + 1).coerceAtMost(59)) }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Seconds")
            }
            Text(
                text = seconds.toString().padStart(2, '0'),
                fontSize = 24.sp
            )

            IconButton(
                onClick = { onSecondsChanged((seconds - 1).coerceAtLeast(0)) }
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Seconds")
            }

            Text("sec", fontSize = 24.sp)
        }
    }
}