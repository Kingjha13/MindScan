package com.example.mindscan.di

import android.content.Context
import androidx.room.Room
import com.example.mindscan.data.local.MindScanDatabase
import com.example.mindscan.data.local.dao.JournalDao
import com.example.mindscan.data.remote.api.GeminiApiService
import com.example.mindscan.domain.repository.JournalRepository
import com.example.mindscan.domain.repository.JournalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindScanDatabase =
        Room.databaseBuilder(context, MindScanDatabase::class.java, "mindscan.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides @Singleton
    fun provideJournalDao(db: MindScanDatabase): JournalDao = db.journalDao()

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides @Singleton
    fun provideGeminiApi(client: OkHttpClient): GeminiApiService =
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)

    @Provides @Singleton
    fun provideJournalRepository(
        dao: JournalDao,
        api: GeminiApiService
    ): JournalRepository = JournalRepositoryImpl(dao, api)
}