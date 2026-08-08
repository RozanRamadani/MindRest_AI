package com.example.features.ikigai.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    @SerialName("system_instruction") val systemInstruction: GeminiContent? = null,
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@Serializable
data class GeminiPart(
    val text: String
)

@Serializable
data class GenerationConfig(
    val responseMimeType: String? = null
)

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
) {
    val text: String?
        get() = candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
}

@Serializable
data class GeminiCandidate(
    val content: GeminiContent? = null
)
