package com.example.mindscan.domain.repository

import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.utils.Result
import kotlinx.coroutines.flow.Flow

interface JournalRepository {

    suspend fun analyzeMood(
        text: String
    ): Result<MoodAnalysis>

    suspend fun saveEntry(
        entry: JournalEntry
    ): Result<Long>

    fun getAllEntries(): Flow<List<JournalEntry>>

    fun getRecentEntries(): Flow<List<JournalEntry>>

    suspend fun deleteEntry(
        entry: JournalEntry
    )

    suspend fun averageMoodScore(
        sinceDaysAgo: Int
    ): Float?
}