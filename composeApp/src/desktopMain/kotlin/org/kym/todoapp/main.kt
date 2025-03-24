package org.kym.todoapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kym.todoapp.di.initKoin

fun main() = application {
    initKoin() // initialize Koin di
    Window(
        onCloseRequest = ::exitApplication,
        title = "TodoApp",
    ) {
        App()
    }
}