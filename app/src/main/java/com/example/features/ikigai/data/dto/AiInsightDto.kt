package com.example.features.ikigai.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class IkigaiResponse(
    val last_analyzed_date: String,
    val user_mood_trend: String,
    val core_ikigai_focus: CoreIkigaiFocus,
    val recommendations: Recommendations
)

@Serializable
data class CoreIkigaiFocus(
    val passion: String,
    val mission: String,
    val vocation: String,
    val profession: String
)

@Serializable
data class Recommendations(
    val daily_activities: List<String>,
    val career_milestones: List<CareerMilestone>,
    val character_growth: String,
    val social_contribution: String
)

@Serializable
data class CareerMilestone(
    val title: String,
    val status: String,
    val impact: String
)
