package com.sonalisulgadle.expensetracker.ui.expense

import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.data.local.CategoryTotal

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val totalSpent: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)