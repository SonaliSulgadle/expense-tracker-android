package com.sonalisulgadle.expensetracker.domain.repository

import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getCategoryTotals(): Flow<List<CategoryTotal>>
    suspend fun addExpense(expense: Expense): Long
    suspend fun deleteExpense(expense: Expense)
}