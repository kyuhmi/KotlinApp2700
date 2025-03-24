package org.kym.todoapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.viewModels.TestViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import todoapp.composeapp.generated.resources.Res
import todoapp.composeapp.generated.resources.compose_multiplatform

// Define Routes (Sealed Class for Destinations)
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{userId}") { // Example with a parameter
        fun createRoute(userId: String) = "detail/$userId"
    }
    data object TestDestination: Screen("test")
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        KoinContext {
            val navController = rememberNavController() // will be passed to screens that need it

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(route = Screen.Home.route) {
                    HomeScreen(navController)
                }
                composable(
                    route = Screen.Detail.route,
                    arguments = listOf(navArgument("userId") { type = NavType.StringType })
                ) { backStackEntry ->
                    // extract userId from nav args
                    val userId = backStackEntry.arguments?.getString("userId") ?: "defaultUser"
                    DetailScreen(navController, userId)
                }
                composable(route = Screen.TestDestination.route) {
                    TestDestinationScreen(navController = navController)
                }
            }
        }
    }
}

// Home Screen
@Composable
fun HomeScreen(navController: NavController) {
    val testViewModel:TestViewModel = koinViewModel<TestViewModel>()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hello World")
            Text(text = testViewModel.repoFindUser("testUser")?.name ?: "User not found")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Screen.Detail.createRoute("user123")) // Navigate with data
            }) {
                Text("Go to Detail (user123)")
            }
            Button(onClick = { navController.navigate(Screen.TestDestination.route)}) {
                Text("Go to Test Destination")
            }
        }
    }
}

// Detail Screen
@Composable
fun DetailScreen(navController: NavController, userId: String) {
    // Example: Get a different ViewModel instance per screen (if needed).  Koin handles this.
    //val detailViewModel = koinViewModel<DetailViewModel>(parameters = { parametersOf(userId) }) //If your DetailViewModel took userID as parameter.
    //For this case, since it is very simple, we just use directly the user id.

    val testViewModel:TestViewModel = koinViewModel<TestViewModel>()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Detail Screen", style = MaterialTheme.typography.h5)
        Text(text = "User ID: $userId")
        Text(text = testViewModel.repoFindUser("testUser")?.name ?: "User not found")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) { // Go back
            Text("Go Back")
        }
    }
}

@Composable
fun TestDestinationScreen(navController: NavController) {
    val viewModel:TestViewModel = koinViewModel<TestViewModel>()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hello from another destination")
            Text(
                text = viewModel.repoFindUser("testUser")?.name ?: "User not found"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) { // Go back
                Text("Go Back")
            }
        }
    }
}

// --- Example resource handling (replace with your actual resource system) ---
object MyResources { // Replace with your Res class if needed
    val composeMultiplatform = "Compose Multiplatform String" // Example string
}

// --- Preview (Optional, good for individual screen previews) ---

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        //  You can't use the navController in a Preview, and this screen requires it.
        //  So, create a "fake" NavController for the Preview.  This is a common pattern.
        HomeScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun DetailScreenPreview(){
    MaterialTheme {
        //  You can't use the navController in a Preview, and this screen requires it.
        //  So, create a "fake" NavController for the Preview.  This is a common pattern.
        DetailScreen(navController = rememberNavController(), userId =  "PreviewUser")
    }
}