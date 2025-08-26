package com.example.aidemo.core.ai

interface TravelAiRepository {
    suspend fun generateTravelPlan(prompt: String): String
}