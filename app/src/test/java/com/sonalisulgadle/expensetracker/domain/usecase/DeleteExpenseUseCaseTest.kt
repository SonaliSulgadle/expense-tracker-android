package com.sonalisulgadle.expensetracker.domain.usecase

import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@DisplayName("DeleteExpenseUseCase")
class DeleteExpenseUseCaseTest {

    private val repository: ExpenseRepository = mock()
    private lateinit var useCase: DeleteExpenseUseCase

    private val fakeExpense = Expense(
        id = 1L,
        description = "burger",
        amount = 8500.0,
        category = "Food & Drink",
        categoryEmoji = "🍔",
        timestamp = System.currentTimeMillis(),
        isAiCategorized = true
    )

    @BeforeEach
    fun setup() {
        useCase = DeleteExpenseUseCase(repository)
    }

    @Nested
    @DisplayName("Delete behavior")
    inner class DeleteBehavior {

        @Test
        @DisplayName("calls repository deleteExpense")
        fun callsRepositoryDelete() = runTest {
            useCase(fakeExpense)
            verify(repository).deleteExpense(fakeExpense)
        }

        @Test
        @DisplayName("passes expense unchanged to repository")
        fun passesExpenseUnchanged() = runTest {
            useCase(fakeExpense)
            verify(repository).deleteExpense(fakeExpense)
        }
    }
}