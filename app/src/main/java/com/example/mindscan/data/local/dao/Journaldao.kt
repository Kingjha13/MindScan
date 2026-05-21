package com.example.mindscan.data.local.dao



import androidx.room.*
import com.example.mindscan.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity): Long

    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: Int): JournalEntryEntity?

    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC LIMIT 7")
    fun getRecentEntries(): Flow<List<JournalEntryEntity>>

    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)

    @Query("SELECT AVG(moodScore) FROM journal_entries WHERE timestamp > :since")
    suspend fun averageMoodSince(since: Long): Float?
}