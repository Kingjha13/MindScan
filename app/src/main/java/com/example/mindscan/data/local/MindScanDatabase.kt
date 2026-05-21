package com.example.mindscan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mindscan.data.local.dao.JournalDao
import com.example.mindscan.data.local.entity.JournalEntryEntity

@Database(
    entities = [JournalEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MindScanDatabase : RoomDatabase() {

    abstract fun journalDao(): JournalDao
}