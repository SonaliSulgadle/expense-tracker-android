package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.ai.GeminiService
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository,
    private val geminiService: GeminiService
) {
    suspend operator fun invoke(
        description: String,
        amount: Double
    ): Result<Expense> {
        return try {
            // Validate inputs first — before calling AI
            if (description.isBlank()) {
                return Result.failure(IllegalArgumentException("Description cannot be empty"))
            }
            if (amount <= 0) {
                return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
            }

            // Ask Gemini to categorize
            val categoryResult = geminiService.categorize(description)

            // Build the domain Expense object
            val expense = Expense(
                description = description.trim(),
                amount = amount,
                category = categoryResult.category,
                categoryEmoji = categoryResult.emoji,
                timestamp = System.currentTimeMillis(),
                isAiCategorized = categoryResult.confidence > 0.0
            )

            // Save to Room via repository
            repository.addExpense(expense)

            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}