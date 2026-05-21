package com.example.mindscan.data.remote.dto

import androidx.annotation.Keep

@Keep
data class MoodAnalysisDto(
    val mood: String,
    val moodScore: Float,
    val emotions: List<String>,
    val insight: String,
    val suggestion: String
)
