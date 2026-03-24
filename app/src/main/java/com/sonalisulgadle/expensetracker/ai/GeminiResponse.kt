package com.sonalisulgadle.expensetracker.ai

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList()
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent
)

@Serializable
data class GeminiError(
    val code: Int = 0,
    val message: String = "",
    val status: String = ""
)