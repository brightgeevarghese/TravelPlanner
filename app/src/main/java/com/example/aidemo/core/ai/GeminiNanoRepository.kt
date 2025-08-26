package com.example.aidemo.core.ai

import com.example.aidemo.core.ai.TravelAiRepository
import com.google.ai.edge.aicore.GenerateContentResponse
import com.google.ai.edge.aicore.GenerativeModel
import kotlinx.coroutines.CancellationException

class GeminiNanoRepository(private val generativeModel: GenerativeModel): TravelAiRepository {
    override suspend fun generateTravelPlan(prompt: String): String {
        try {
            val response = generativeModel.generateContent(prompt)
            return response.text ?: ""
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            return "Error: ${e.message}"
        }
    }
}