package com.sonalisulgadle.expensetracker.domain.model

data class Expense(
    val id: Long = 0,
    val description: String,
    val amount: Double,
    val category: String,
    val categoryEmoji: String,
    val timestamp: Long,
    val isAiCategorized: Boolean
)