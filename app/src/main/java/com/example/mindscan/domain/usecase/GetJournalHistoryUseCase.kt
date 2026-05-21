package com.example.mindscan.domain.usecase

import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetJournalHistoryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    operator fun invoke(): Flow<List<JournalEntry>> = repository.getAllEntries()
}
