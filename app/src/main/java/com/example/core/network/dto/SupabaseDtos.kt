package com.example.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryInsert(
    @SerialName("user_id") val userId: String,
    @SerialName("content") val content: String
)

@Serializable
data class SleepLogInsert(
    @SerialName("user_id") val userId: String,
    @SerialName("bed_time") val bedTime: String,
    @SerialName("wake_up_time") val wakeUpTime: String,
    @SerialName("sleep_quality") val sleepQuality: String
)

@Serializable
data class MoodLogInsert(
    @SerialName("user_id") val userId: String,
    @SerialName("mood_score") val moodScore: Int
)
