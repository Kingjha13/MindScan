package com.example.mindscan.data.remote.api


import com.example.mindscan.data.remote.dto.GeminiRequest
import com.example.mindscan.data.remote.dto.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {

    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun analyzeText(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
