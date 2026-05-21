package com.example.mindscan.domain.repository


import com.example.mindscan.data.local.dao.JournalDao
import com.example.mindscan.data.local.entity.JournalEntryEntity
import com.example.mindscan.data.remote.api.GeminiApiService
import com.example.mindscan.data.remote.dto.GeminiRequest
import com.example.mindscan.data.remote.dto.MoodAnalysisDto
import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.utils.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val dao: JournalDao,
    private val geminiApi: GeminiApiService
) : JournalRepository {
    private val apiKey = "YOUR_API_KEY_HERE"

    override suspend fun analyzeMood(text: String): Result<MoodAnalysis> {
        return try {

            val prompt = buildPrompt(text)

            val request = GeminiRequest(
                contents = listOf(
                    GeminiRequest.Content(
                        parts = listOf(
                            GeminiRequest.Part(prompt)
                        )
                    )
                )
            )

            val response = geminiApi.analyzeText(
                apiKey = apiKey,
                request = request
            )

            val rawText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: return Result.Error("Empty response")

            val json = rawText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val dto = Gson().fromJson(json, MoodAnalysisDto::class.java)

            Result.Success(dto.toDomain())

        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    private fun buildPrompt(journalText: String): String {

        return """
        You are an empathetic AI mood analyst.

        Analyze the following journal entry and respond ONLY with valid JSON.

        Journal entry:
        "$journalText"

        Respond with exactly this JSON structure:

        {
          "mood": "one word mood label",
          "moodScore": 0.75,
          "emotions": ["emotion1", "emotion2"],
          "insight": "empathetic insight",
          "suggestion": "actionable suggestion"
        }

        Rules:
        - moodScore between 0.0 and 1.0
        - emotions must contain 2-4 emotions
        - no markdown
        - no explanation
        """.trimIndent()
    }

    override suspend fun saveEntry(
        entry: JournalEntry
    ): Result<Long> {

        return try {

            val id = dao.insertEntry(
                entry.toEntity()
            )

            Result.Success(id)

        } catch (e: Exception) {

            Result.Error(
                e.message ?: "Failed to save"
            )
        }
    }

    override fun getAllEntries(): Flow<List<JournalEntry>> {

        return dao.getAllEntries().map { list ->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getRecentEntries(): Flow<List<JournalEntry>> {

        return dao.getRecentEntries().map { list ->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteEntry(
        entry: JournalEntry
    ) {

        dao.deleteEntry(
            entry.toEntity()
        )
    }

    override suspend fun averageMoodScore(
        sinceDaysAgo: Int
    ): Float? {

        val since = System.currentTimeMillis() -
                TimeUnit.DAYS.toMillis(
                    sinceDaysAgo.toLong()
                )

        return dao.averageMoodSince(since)
    }

    private fun JournalEntryEntity.toDomain(): JournalEntry {

        return JournalEntry(
            id = id,
            content = content,
            mood = mood,
            moodScore = moodScore,
            aiInsight = aiInsight,
            aiSuggestion = aiSuggestion,
            emotions = emotions
                .split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() },
            timestamp = timestamp
        )
    }

    private fun JournalEntry.toEntity(): JournalEntryEntity {

        return JournalEntryEntity(
            id = id,
            content = content,
            mood = mood,
            moodScore = moodScore,
            aiInsight = aiInsight,
            aiSuggestion = aiSuggestion,
            emotions = emotions.joinToString(","),
            timestamp = timestamp
        )
    }

    private fun MoodAnalysisDto.toDomain(): MoodAnalysis {

        return MoodAnalysis(
            mood = mood,
            moodScore = moodScore,
            emotions = emotions,
            insight = insight,
            suggestion = suggestion
        )
    }
}