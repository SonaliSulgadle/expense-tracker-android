package com.sonalisulgadle.expensetracker.ui.category

import com.sonalisulgadle.expensetracker.domain.model.Expense

data class CategoryDetailUiState(
    val categoryName: String = "",
    val categoryEmoji: String = "",
    val totalSpent: Double = 0.0,
    val expenseCount: Int = 0,
    val averageExpense: Double = 0.0,
    val highestExpense: Double = 0.0,
    val percentageOfTotal: Double = 0.0,
    val topItems: List<TopItem> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true
)

data class TopItem(
    val description: String,
    val amount: Double,
    val percentage: Float
)