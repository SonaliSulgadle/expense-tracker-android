package com.sonalisulgadle.expensetracker.ai

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResult(
    val category: String,
    val emoji: String,
    val confidence: Double
)