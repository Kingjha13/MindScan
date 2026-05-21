package com.example.mindscan.domain.usecase

import com.example.mindscan.domain.repository.JournalRepository
import javax.inject.Inject

class GetMoodStatsUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend fun weeklyAverage(): Float? = repository.averageMoodScore(7)
    suspend fun monthlyAverage(): Float? = repository.averageMoodScore(30)
}
