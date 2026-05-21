package com.example.mindscan.domain.model

data class JournalEntry(
    val id: Int = 0,
    val content: String,
    val mood: String,
    val moodScore: Float,
    val aiInsight: String,
    val aiSuggestion: String,
    val emotions: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)
