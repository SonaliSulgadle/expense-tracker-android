package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.model.ExpenseError
import com.sonalisulgadle.expensetracker.domain.repository.CategoryRepository
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

private const val MAX_DESCRIPTION_LENGTH = 200
private const val MAX_AMOUNT = 999_999_999.0

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
                return Result.failure(ExpenseError.EmptyDescription())
            }
            if (description.length > MAX_DESCRIPTION_LENGTH) {
                return Result.failure(ExpenseError.DescriptionTooLong())
            }
            if (amount <= 0) {
                return Result.failure(ExpenseError.InvalidAmount())
            }
            if (amount > MAX_AMOUNT) {
                return Result.failure(ExpenseError.AmountTooLarge())
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
            Result.failure(ExpenseError.DatabaseError())
        }
    }
}