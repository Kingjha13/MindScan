package com.example.mindscan.domain.usecase

import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.domain.repository.JournalRepository
import com.example.mindscan.utils.Result
import javax.inject.Inject

class AnalyzeMoodUseCase @Inject constructor(
    private val repository: JournalRepository
) {

    suspend operator fun invoke(text: String): Result<MoodAnalysis> {

        return if (text.trim().length < 10) {

            Result.Error(
                "Please write at least a few sentences for a better analysis."
            )

        } else {

            repository.analyzeMood(text)
        }
    }
}