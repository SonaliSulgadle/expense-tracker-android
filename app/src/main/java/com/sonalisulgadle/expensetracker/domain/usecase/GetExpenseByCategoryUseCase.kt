package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetExpensesByCategoryUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(category: String): Flow<List<Expense>> =
        repository.getAllExpenses().map { expenses ->
            expenses.filter { it.category == category }
        }
}