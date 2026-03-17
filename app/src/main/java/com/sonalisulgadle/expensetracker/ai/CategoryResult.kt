package com.sonalisulgadle.expensetracker.ai

import kotlinx.serialization.Serializable

// For Gemini response
@Serializable
data class GeminiCategoryResponse(
    val category: String,
    val confidence: Double
)

// What the rest of the app sees — with emoji
data class CategoryResult(
    val category: String,
    val emoji: String,
    val confidence: Double
)