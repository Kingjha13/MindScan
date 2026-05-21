package com.example.mindscan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val mood: String,
    val moodScore: Float,
    val aiInsight: String,
    val aiSuggestion: String,
    val emotions: String,
    val timestamp: Long = System.currentTimeMillis()
)

