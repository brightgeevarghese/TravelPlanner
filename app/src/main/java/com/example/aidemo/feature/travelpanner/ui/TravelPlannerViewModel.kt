package com.example.aidemo.feature.travelpanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidemo.core.ai.TravelAiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TravelPlannerViewModel(private val travelAiRepository: TravelAiRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TravelPlannerUiState())
    val uiState: StateFlow<TravelPlannerUiState> = _uiState.asStateFlow()

    fun promptChanged(newPrompt: String) {
        _uiState.update { it.copy(prompt = newPrompt, error = null) }
    }

    fun generateTravelPlan() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val response = travelAiRepository.generateTravelPlan(
                """
                    Prompt: Generate a short tour plan for the following request. The plan should have 2–5 bullets.
                Each bullet is one concise sentence. Output only the plan, no extra explanation.

                input: 2-day trip to Paris
                output:
                - Day 1: Visit the Eiffel Tower and enjoy a Seine River cruise.
                - Day 2: Explore the Louvre Museum and stroll through Montmartre.

                input: 3 days in Rome
                output:
                - Day 1: Explore the Colosseum and Roman Forum.
                - Day 2: Visit the Vatican Museums and St. Peter’s Basilica.
                - Day 3: Walk through the Pantheon and toss a coin at Trevi Fountain.

                input: ${uiState.value.prompt}
                """.trimIndent()
            )
            _uiState.update {
                if (response.startsWith("Error")) {
                    it.copy(isLoading = false, error = response)
                } else {
                    it.copy(response = response, isLoading = false)
                }
            }
        }
    }
}