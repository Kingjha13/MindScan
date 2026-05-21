package com.example.mindscan.data.remote.dto

import androidx.annotation.Keep

@Keep
data class GeminiRequest(
    val contents: List<Content>
) {
    @Keep
    data class Content(val parts: List<Part>)
    @Keep
    data class Part(val text: String)
}

@Keep
data class GeminiResponse(
    val candidates: List<Candidate>?
) {
    @Keep
    data class Candidate(val content: Content?)
    @Keep
    data class Content(val parts: List<Part>?)
    @Keep
    data class Part(val text: String?)
}
