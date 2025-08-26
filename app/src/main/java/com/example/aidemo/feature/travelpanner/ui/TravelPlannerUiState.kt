package com.example.aidemo.feature.travelpanner.ui

data class TravelPlannerUiState(
    val prompt: String = "",
    val response: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
