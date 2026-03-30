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

@DisplayName("RestoreExpenseUseCase")
class RestoreExpenseUseCaseTest {

    private val repository: ExpenseRepository = mock()
    private lateinit var useCase: RestoreExpenseUseCase

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
        useCase = RestoreExpenseUseCase(repository)
    }

    @Nested
    @DisplayName("Restore behavior")
    inner class RestoreBehavior {

        @Test
        @DisplayName("calls repository addExpense with original expense")
        fun callsRepositoryAddExpense() = runTest {
            useCase(fakeExpense)
            verify(repository).addExpense(fakeExpense)
        }

        @Test
        @DisplayName("preserves original category on restore")
        fun preservesOriginalCategory() = runTest {
            val expenseWithCategory = fakeExpense.copy(
                category = "Transport",
                categoryEmoji = "🚕"
            )
            useCase(expenseWithCategory)
            verify(repository).addExpense(expenseWithCategory)
        }

        @Test
        @DisplayName("preserves isAiCategorized flag on restore")
        fun preservesAiFlag() = runTest {
            useCase(fakeExpense.copy(isAiCategorized = true))
            verify(repository).addExpense(
                org.mockito.kotlin.argThat { isAiCategorized }
            )
        }
    }
}