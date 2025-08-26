package com.example.aidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.aidemo.core.di.ServiceLocator
import com.example.aidemo.feature.travelpanner.ui.PromptScreen
import com.example.aidemo.feature.travelpanner.ui.TravelPlannerViewModel
import com.example.aidemo.ui.theme.AidemoTheme
import com.google.ai.edge.aicore.DownloadConfig
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AidemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PromptScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TravelPlannerScreen(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var response by remember { mutableStateOf("Generating travel plan...") }
    val generationConfig = generationConfig {
        this.context = context.applicationContext // required
        temperature = 0.2f
        topK = 16
        maxOutputTokens = 256
    }
    val downloadConfig = DownloadConfig()
    val generativeModel = GenerativeModel(
        generationConfig = generationConfig,
        downloadConfig = downloadConfig // optional
    )
    LaunchedEffect(Unit) {
        val input = """
            Prompt: Generate a short tour plan for Fairfield, Iowa. The plan should have 2–5 bullets. Each bullet is one concise sentence. Output only the plan, no extra explanation.

            input: 2-day trip to Paris
            output:
            - Day 1: Visit the Eiffel Tower and enjoy a Seine River cruise.
            - Day 2: Explore the Louvre Museum and stroll through Montmartre.

            input: 3 days in Rome
            output:
            - Day 1: Explore the Colosseum and Roman Forum.
            - Day 2: Visit the Vatican Museums and St. Peter’s Basilica.
            - Day 3: Walk through the Pantheon and toss a coin at Trevi Fountain.


            """.trimIndent()
        withContext(Dispatchers.IO) {
            // Single string input prompt
            val result = withContext(Dispatchers.IO) {
                generativeModel.generateContent(input)
            }
            response = result.text ?: "No response"
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "AI Tour Planner",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "The AI model is running on-device and will output the text below.")
        Text(text = response)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AidemoTheme {
        TravelPlannerScreen("Android")
    }
}