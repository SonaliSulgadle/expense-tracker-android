package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(
        description: String,
        amount: Double
    ): Result<Expense> {
        return try {
            if (description.isBlank()) {
                return Result.failure(IllegalArgumentException("Description cannot be empty"))
            }
            if (amount <= 0) {
                return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
            }

            val categoryResult = categoryRepository.categorize(description)

            val expense = Expense(
                description = description.trim(),
                amount = amount,
                category = categoryResult.category,
                categoryEmoji = categoryResult.emoji,
                timestamp = System.currentTimeMillis(),
                isAiCategorized = categoryResult.confidence > 0.0
            )

            expenseRepository.addExpense(expense)

            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}