package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.data.local.CategoryTotal
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryTotalsUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<CategoryTotal>> =
        repository.getCategoryTotals()
}