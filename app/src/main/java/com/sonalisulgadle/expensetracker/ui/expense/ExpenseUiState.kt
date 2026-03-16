package com.sonalisulgadle.expensetracker.ui.expense

import com.sonalisulgadle.expensetracker.domain.model.Expense

sealed class ExpenseUiState {
    object Idle : ExpenseUiState()
    object Categorizing : ExpenseUiState()
    object Loading : ExpenseUiState()
    data class Success(val expense: Expense) : ExpenseUiState()
    data class Error(val messageRes: Int) : ExpenseUiState()
}