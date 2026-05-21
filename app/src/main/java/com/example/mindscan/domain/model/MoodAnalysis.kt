package com.example.mindscan.domain.model

data class MoodAnalysis(
    val mood: String,
    val moodScore: Float,
    val emotions: List<String>,
    val insight: String,
    val suggestion: String
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}