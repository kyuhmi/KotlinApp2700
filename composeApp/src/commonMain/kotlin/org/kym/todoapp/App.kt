package org.kym.todoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.ui.DetailScreen
import org.kym.todoapp.ui.HomeScreen
import org.kym.todoapp.ui.Screens
import org.kym.todoapp.ui.TestDestinationScreen
import org.kym.todoapp.viewModels.TestViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            val navController = rememberNavController() // will be passed to screens that need it

            NavHost(
                navController = navController,
                startDestination = Screens.Home.route
            ) {
                composable(route = Screens.Home.route) {
                    HomeScreen(navController)
                }
                composable(
                    route = Screens.Detail.route,
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { backStackEntry ->
                    // extract userId from nav args
                    val userId = backStackEntry.arguments?.getString("userId") ?: "defaultUser"
                    DetailScreen(navController, userId)
                }
                composable(route = Screens.TestDestination.route) {
                    TestDestinationScreen(navController = navController)
                }
            }
        }
    }
}

// --- Example resource handling (replace with your actual resource system) ---
object MyResources { // Replace with your Res class if needed
    val composeMultiplatform = "Compose Multiplatform String" // Example string
}