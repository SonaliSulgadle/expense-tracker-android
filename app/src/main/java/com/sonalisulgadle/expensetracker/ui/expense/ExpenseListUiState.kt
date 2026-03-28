package com.sonalisulgadle.expensetracker.ui.expense

import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.domain.model.Expense

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val groupedExpenses: Map<String, List<Expense>> = emptyMap(),
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val totalSpent: Double = 0.0,
    val avgPerDay: Double = 0.0,
    val currentMonth: String = "",
    val userName: String = "",
    val userInitial: String = "",
    val isLoading: Boolean = false,
    val error: Int? = null
)