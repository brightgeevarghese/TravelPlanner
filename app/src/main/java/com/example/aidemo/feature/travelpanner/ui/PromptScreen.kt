package com.example.aidemo.feature.travelpanner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aidemo.core.di.ServiceLocator

@Composable
fun PromptScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val context = LocalContext.current
        val viewModel: TravelPlannerViewModel = viewModel {
            TravelPlannerViewModel(
                ServiceLocator.getTravelAiRepository(context)
            )
        }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        OutlinedTextField(
            value = uiState.prompt,
            onValueChange = { viewModel.promptChanged(it) },
            label = { Text("Enter your prompt") },
            placeholder = { Text("e.g. 2-day trip to Paris") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::generateTravelPlan,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Generate Travel Plan")
        }

        when {
            uiState.isLoading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            uiState.response.isNotEmpty() -> {
                Text(text = uiState.response)
            }
            uiState.error != null -> {
                Text(text = "Error: ${uiState.error}")
            }
            else -> {
                Text(text = "Type a prompt and generate a travel plan.")
            }
        }
    }
}