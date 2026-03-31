package com.sonalisulgadle.expensetracker.domain.usecase

import app.cash.turbine.test
import com.sonalisulgadle.expensetracker.domain.model.Expense
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("GetExpensesUseCase")
class GetExpensesUseCaseTest {

    private val repository: ExpenseRepository = mock()
    private lateinit var useCase: GetExpensesUseCase

    private val fakeExpenses = listOf(
        Expense(
            1L, "burger", 8500.0, "Food & Drink",
            "🍔", System.currentTimeMillis(), true
        ),
        Expense(
            2L, "uber", 15000.0, "Transport",
            "🚕", System.currentTimeMillis(), true
        )
    )

    @BeforeEach
    fun setup() {
        useCase = GetExpensesUseCase(repository)
    }

    @Nested
    @DisplayName("Expense retrieval")
    inner class ExpenseRetrieval {

        @Test
        @DisplayName("returns all expenses from repository")
        fun returnsAllExpenses() = runTest {
            whenever(repository.getAllExpenses())
                .thenReturn(flowOf(fakeExpenses))

            useCase().test {
                val items = awaitItem()
                assertEquals(2, items.size)
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("returns empty list when no expenses")
        fun returnsEmptyList() = runTest {
            whenever(repository.getAllExpenses())
                .thenReturn(flowOf(emptyList()))

            useCase().test {
                assertTrue(awaitItem().isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("delegates to repository")
        fun delegatesToRepository() = runTest {
            whenever(repository.getAllExpenses())
                .thenReturn(flowOf(emptyList()))

            useCase().test {
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }

            verify(repository).getAllExpenses()
        }
    }
}