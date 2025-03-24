package org.kym.todoapp.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PomodoroScreen(navController: NavController) {


}

@Preview
@Composable
fun PomodoroScreenPreview() {
    MaterialTheme {
        PomodoroScreen(navController = rememberNavController())
    }
}