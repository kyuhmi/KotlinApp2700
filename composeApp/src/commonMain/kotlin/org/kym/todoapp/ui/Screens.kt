package org.kym.todoapp.ui

// Define Routes (Sealed Class for Destinations)
sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Detail : Screens("detail/{userId}") { // Example with a parameter
        fun createRoute(userId: String) = "detail/$userId"
    }
    data object TestDestination: Screens("test")
    data object Pomodoro: Screens("pomodoro")
    data object PomodoroSettings: Screens("pomodoroSettings")
}