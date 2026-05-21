package com.example.mindscan.domain.usecase

import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.domain.repository.JournalRepository
import com.example.mindscan.utils.Result
import javax.inject.Inject

class SaveJournalUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(
        content: String,
        analysis: MoodAnalysis
    ): Result<Long> {
        val entry = JournalEntry(
            content = content,
            mood = analysis.mood,
            moodScore = analysis.moodScore,
            aiInsight = analysis.insight,
            aiSuggestion = analysis.suggestion,
            emotions = analysis.emotions
        )
        return repository.saveEntry(entry)
    }
}