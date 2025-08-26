package com.example.aidemo.core.di

import android.content.Context
import com.example.aidemo.core.ai.GeminiNanoRepository
import com.example.aidemo.core.ai.TravelAiRepository
import com.google.ai.edge.aicore.DownloadConfig
import com.google.ai.edge.aicore.GenerationConfig
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import kotlin.concurrent.Volatile

object ServiceLocator {
    @Volatile
    private var generativeModel: GenerativeModel?= null
    @Volatile
    private var travelAiRepository: TravelAiRepository?= null
    fun getGenerativeModel(context: Context): GenerativeModel {
        val generationConfig: GenerationConfig = generationConfig {
            this.context = context
            temperature = 0.2f
            topK = 16
            maxOutputTokens = 256
        }
        val downloadConfig = DownloadConfig()
        return generativeModel ?: synchronized(this) {
            GenerativeModel(generationConfig, downloadConfig)
        }
    }

    fun getTravelAiRepository(context: Context): TravelAiRepository {
        return travelAiRepository ?: synchronized(this) {
            GeminiNanoRepository(getGenerativeModel(context))
        }
    }

}