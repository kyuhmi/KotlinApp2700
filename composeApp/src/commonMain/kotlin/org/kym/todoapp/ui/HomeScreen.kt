package org.kym.todoapp.ui

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
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.viewModels.TestViewModel

// Home Screen
@Composable
fun HomeScreen(navController: NavController) {
    val testViewModel: TestViewModel = koinViewModel<TestViewModel>()
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
                navController.navigate(Screens.Detail.createRoute("user123")) // Navigate with data
            }) {
                Text("Go to Detail (user123)")
            }
            Button(onClick = { navController.navigate(Screens.TestDestination.route)}) {
                Text("Go to Test Destination")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        //  You can't use the navController in a Preview, and this screen requires it.
        //  So, create a "fake" NavController for the Preview.  This is a common pattern.
        HomeScreen(navController = rememberNavController())
    }
}
