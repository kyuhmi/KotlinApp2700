package org.kym.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

// Detail Screen
@Composable
fun DetailScreen(navController: NavController, userId: String) {
    // Example: Get a different ViewModel instance per screen (if needed).  Koin handles this.
    //val detailViewModel = koinViewModel<DetailViewModel>(parameters = { parametersOf(userId) }) //If your DetailViewModel took userID as parameter.
    //For this case, since it is very simple, we just use directly the user id.

    val testViewModel: TestViewModel = koinViewModel<TestViewModel>()

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

@Preview
@Composable
fun DetailScreenPreview(){
    MaterialTheme {
        //  You can't use the navController in a Preview, and this screen requires it.
        //  So, create a "fake" NavController for the Preview.  This is a common pattern.
        DetailScreen(navController = rememberNavController(), userId =  "PreviewUser")
    }
}
