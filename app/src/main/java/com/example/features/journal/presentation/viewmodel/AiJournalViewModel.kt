package com.example.features.journal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.features.ikigai.data.dto.GeminiContent
import com.example.features.ikigai.data.dto.GeminiPart
import com.example.features.ikigai.data.dto.GeminiRequest
import com.example.features.ikigai.data.dto.GeminiResponse
import com.example.features.ikigai.data.dto.GenerationConfig
import com.example.features.journal.presentation.screen.ChatMessage
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.features.journal.data.repository.JournalRepository
import com.example.features.journal.data.repository.JournalRepositoryImpl
import com.example.core.network.dto.JournalEntryInsert
import com.example.core.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class AiJournalViewModel(
    private val repository: JournalRepository = JournalRepositoryImpl()
) : ViewModel() {
    private val client = HttpClient(OkHttp)
    private val json = Json { ignoreUnknownKeys = true }

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "Hai, aku perhatikan hari ini pikiranmu sedang berat. Ingin menceritakan pelan-pelan apa yang mengganggumu?",
                isFromAi = true,
                time = "Now",
                isLoading = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onMessageShown() {
        _errorMessage.value = null
        _saveSuccess.value = false
    }

    fun summarizeAndSaveChat() {
        viewModelScope.launch(Dispatchers.IO) {
            _isSaving.value = true
            try {
                val currentMessages = _messages.value.filterNot { it.isLoading }
                val chatHistoryText = currentMessages.joinToString("\n") { 
                    "${if (it.isFromAi) "AI" else "User"}: ${it.text}" 
                }

                val systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "Summarize the core emotional points and events from this conversation into a single, concise paragraph written in the first person ('Saya'). Do not add any conversational filler."
                        )
                    )
                )

                val content = GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = "Chat History:\n$chatHistoryText"))
                )

                val requestObj = GeminiRequest(
                    systemInstruction = systemInstruction,
                    contents = listOf(content),
                    generationConfig = GenerationConfig(responseMimeType = "text/plain")
                )

                val requestJson = json.encodeToString(requestObj)
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
                
                val response = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(requestJson)
                }

                val responseBody = response.bodyAsText()
                val geminiResponse = json.decodeFromString<GeminiResponse>(responseBody)
                val summaryText = geminiResponse.text ?: throw Exception("Failed to generate summary")

                val userId = SupabaseClient.client.auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")

                val entry = JournalEntryInsert(
                    userId = userId,
                    content = "AI Chat Summary: $summaryText"
                )

                val saveResult = repository.insertJournalEntry(entry)
                if (saveResult.isSuccess) {
                    _saveSuccess.value = true
                } else {
                    throw saveResult.exceptionOrNull() ?: Exception("Unknown error saving journal")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = e.message ?: "Failed to summarize and save chat"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = userText,
            isFromAi = false,
            time = "Now",
            isLoading = false
        )
        
        val loadingMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = "Sedang mengetik...",
            isFromAi = true,
            time = "Now",
            isLoading = true
        )

        _messages.update { it + userMessage + loadingMessage }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Build history for API request
                val currentMessages = _messages.value.filterNot { it.isLoading }
                
                // Map existing chat messages to Gemini contents (roles: user, model)
                val contents = currentMessages.map { msg ->
                    GeminiContent(
                        role = if (msg.isFromAi) "model" else "user",
                        parts = listOf(GeminiPart(text = msg.text))
                    )
                }

                val systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "Kamu adalah pendengar yang penuh empati dan fokus pada terapi kognitif perilaku (CBT). Berikan validasi atas perasaan pengguna, tanyakan pertanyaan reflektif yang membantu mereka memahami pikirannya sendiri. Jaga agar respons tetap singkat (1-3 kalimat) sehingga terasa seperti obrolan manusia sungguhan."
                        )
                    )
                )

                val requestObj = GeminiRequest(
                    systemInstruction = systemInstruction,
                    contents = contents,
                    generationConfig = GenerationConfig(responseMimeType = "text/plain")
                )

                val requestJson = json.encodeToString(requestObj)

                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
                
                val response = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(requestJson)
                }

                val responseBody = response.bodyAsText()
                val geminiResponse = json.decodeFromString<GeminiResponse>(responseBody)
                val aiText = geminiResponse.text ?: "Maaf, saya tidak dapat merespons saat ini."

                _messages.update { current ->
                    val filtered = current.filterNot { it.isLoading }
                    filtered + ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = aiText,
                        isFromAi = true,
                        time = "Now",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _messages.update { current ->
                    val filtered = current.filterNot { it.isLoading }
                    filtered + ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = "Terjadi kesalahan: ${e.message}",
                        isFromAi = true,
                        time = "Now",
                        isLoading = false
                    )
                }
            }
        }
    }
}
