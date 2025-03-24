package org.kym.todoapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.kym.todoapp.viewModels.TestViewModel

@Composable
fun TestDestinationScreen(navController: NavController) {
    val viewModel: TestViewModel = koinViewModel<TestViewModel>()
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
