package com.example.features.ikigai.data.repository

import com.example.BuildConfig
import com.example.core.network.dto.JournalEntry
import com.example.core.network.dto.MoodLog
import com.example.features.ikigai.data.dto.IkigaiResponse
import com.example.features.ikigai.data.dto.GeminiRequest
import com.example.features.ikigai.data.dto.GeminiContent
import com.example.features.ikigai.data.dto.GeminiPart
import com.example.features.ikigai.data.dto.GenerationConfig
import com.example.features.ikigai.data.dto.GeminiResponse
import com.example.features.ikigai.domain.repository.AiInsightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class AiInsightRepositoryImpl : AiInsightRepository {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    override suspend fun generateIkigaiDashboard(
        journals: List<JournalEntry>,
        moodLogs: List<MoodLog>
    ): Result<IkigaiResponse> = withContext(Dispatchers.IO) {
        try {
            val journalsText = journals.joinToString("\n") { 
                "Date: ${it.createdAt}, Content: ${it.content}" 
            }
            val moodLogsText = moodLogs.joinToString("\n") { 
                "Date: ${it.createdAt}, Score: ${it.moodScore}" 
            }

            val promptText = """
                You are an expert Cognitive Behavioral Therapy (CBT) and Ikigai analyst.
                Analyze the following user's historical journals and mood logs to generate a personalized Ikigai recommendation dashboard.
                
                Journals:
                $journalsText
                
                Mood Logs:
                $moodLogsText
                
                You must output ONLY a raw JSON string that strictly matches the following schema:
                {
                    "last_analyzed_date": "YYYY-MM-DD",
                    "user_mood_trend": "Brief description of the user's mood trend",
                    "core_ikigai_focus": {
                        "passion": "...",
                        "mission": "...",
                        "vocation": "...",
                        "profession": "..."
                    },
                    "recommendations": {
                        "daily_activities": ["activity1", "activity2"],
                        "career_milestones": [
                            {"title": "...", "status": "...", "impact": "..."}
                        ],
                        "character_growth": "...",
                        "social_contribution": "..."
                    }
                }
            """.trimIndent()

            val requestBodyObj = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = promptText))
                    )
                ),
                generationConfig = GenerationConfig(
                    responseMimeType = "application/json"
                )
            )

            val requestBodyStr = json.encodeToString(requestBodyObj)
            val requestBody = requestBodyStr.toRequestBody(JSON_MEDIA_TYPE)

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${BuildConfig.GEMINI_API_KEY}")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                throw Exception("HTTP Error ${response.code}: $errorBody")
            }

            val responseBodyStr = response.body?.string() ?: throw Exception("Empty response body from Gemini")
            val geminiResponse = json.decodeFromString<GeminiResponse>(responseBodyStr)
            val responseText = geminiResponse.text ?: throw Exception("Gemini returned no text content")
            
            // Clean up the response text just in case the model returns markdown code blocks
            val cleanedText = responseText
                .removePrefix("```json\n")
                .removePrefix("```\n")
                .removeSuffix("\n```")
                .trim()

            val ikigaiResponse = json.decodeFromString<IkigaiResponse>(cleanedText)
            Result.success(ikigaiResponse)
        } catch (e: SerializationException) {
            Result.failure(Exception("Failed to parse JSON response: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to generate Ikigai dashboard: ${e.message}", e))
        }
    }
}
